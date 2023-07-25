import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { MatIconRegistry, MatIconModule } from '@angular/material/icon';
import { COLD_ICON, CONFINED_ICON, HOT_ICON } from '../_ModelandConstants/iconConstant';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { COMPANIES, LOCATIONS, OPTIONS } from '../_ModelandConstants/constant';
import { Observable } from 'rxjs/internal/Observable';
import { map, startWith } from 'rxjs/operators';
import { DatePipe, formatDate } from '@angular/common';
import { HttpClient } from "@angular/common/http";
import { PTWService } from '../_Service/PTW.service';
import { Permit, SearchQuery, User_Registeration } from '../_ModelandConstants/model';
import { MatTabGroup } from '@angular/material/tabs';
import { ActivatedRoute, Router } from '@angular/router';
import { ChartService } from '../_Service/Chart.service';

//SVG link
const hot = HOT_ICON
const cold = COLD_ICON
const confined = CONFINED_ICON



//variable
@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {

  //View Componenet for tab Search
  @ViewChild('tabGroup') tabGroup!: MatTabGroup


  // bring in the constants
  options = OPTIONS
  locations = LOCATIONS
  pendingLocations!: string[]
  approvedLocations!: string[]


  //FormControl for autocomplete
  typeControl = new FormControl('', [Validators.required, Validators.minLength(1)])
  companyControl = new FormControl('')
  locationControl = new FormControl('Production', [Validators.required, Validators.minLength(1)])


  //FormControl for searches (pending, approved, closed, cancelled)
  statusSearchControl = new FormControl('pending')
  idControl = new FormControl<number | null>(null)

  //implementing observables
  filteredOptions!: Observable<string[]> //input company name
  filteredLocations!: Observable<string[]> //input location name

  // @Autowire inject >= v14
  fb: FormBuilder = inject(FormBuilder)
  iconRegistry: MatIconRegistry = inject(MatIconRegistry)
  sanitizer: DomSanitizer = inject(DomSanitizer)
  router: Router = inject(Router)
 
  ptwSvc = inject(PTWService)
  chartSvc = inject(ChartService)

  constructor(private route: ActivatedRoute) { }

  //form
  Form!: FormGroup
  pendingGroup!: FormGroup
  approvedGroup!: FormGroup

  //variable + Observable
  Response!: String
  pendingpermits$!: Observable<Permit[]>
  approvedpermits$!: Observable<Permit[]>
  searchState: string = ''
  permitNumber!: number
  user!: User_Registeration

  ngOnInit(): void {
    /*----------------instanitate the pending & approved locations------------------------*/
    this.chartSvc.getInfoLocation(null).subscribe(
      (response: any) => {
        // Handle the response here
        this.pendingLocations = response.location
        console.log('Current locations:', this.pendingLocations);

      },
      (error: any) => {
        // Handle the error here
        console.error('Error occurred during searchPTWbyId:', error);
      }
    );

    this.chartSvc.getapprovedInfoLocation().subscribe(
      (response: any) => {
        // Handle the response here
        this.approvedLocations = response.location
        console.log('Current locations:', this.approvedLocations);

      },
      (error: any) => {
        // Handle the error here
        console.error('Error occurred during searchPTWbyId:', error);
      }
    );

    /*----------------Create------------------------*/

    //linking the svg file to the respective tag in html
    this.iconRegistry.addSvgIconLiteral('hot', this.sanitizer.bypassSecurityTrustHtml(hot))
    this.iconRegistry.addSvgIconLiteral('cold', this.sanitizer.bypassSecurityTrustHtml(cold))
    this.iconRegistry.addSvgIconLiteral('confined', this.sanitizer.bypassSecurityTrustHtml(confined))

    // linking the company name input observable to companyControlgroup. If there is any changes, it will be process as lowercase to match  
    this.filteredOptions = this.companyControl.valueChanges.pipe(
      startWith(''),
      map(value => this._optionfilter(value || '')),
    );
    this.filteredLocations = this.locationControl.valueChanges.pipe(
      startWith(''),
      map(value => this._locationfilter(value || '')),
    );
    // instantitae the form with the formbuilder
    this.Form = this.createFormWithFormBuilder()


    /*----------------Search -----------------------*/
    this.pendingGroup = this.createFormSearch()
    this.pendingpermits$ = this.ptwSvc.searchPTW(this.pendingGroup.value)
    this.approvedGroup = this.createFormSearch()
    this.approvedpermits$ = this.ptwSvc.searchPTW(this.pendingGroup.value)

    /*---------------------user details--------------------------*/
    
    const currentUser = localStorage.getItem('currentUser')
    if (currentUser) {
      this.user = JSON.parse(currentUser)
      
    } else {
      this.router.navigate(['/login']);

    }




  }

  invalidForm() {
    return this.Form.invalid
  }

  selectType(category: string) {
    this.typeControl.setValue(category)
    console.log(this.typeControl)
  }

  onCreateEdit() {
    if (this.idControl.value === null) {
      this.searchState = "create";
      this.Form.reset();
    } else {
      this.searchState = "edit";
      console.info('>> edit request Id: ', this.idControl.value);
      this.ptwSvc.searchPTWbyId(this.idControl.value).subscribe(
        (response: Permit) => {
          // Handle the response here
          console.log('Response from searchPTWbyId:', response);
          this.populateForm(response);
        },
        (error: any) => {
          // Handle the error here
          console.error('Error occurred during searchPTWbyId:', error);
        }
      );
    }
  }


  cancelCreateEdit() {
    this.searchState = ''
    this.Form.reset
    this.idControl.setValue(null)
  }

  /*----------------------------------------------*/
  /*----------------Create -----------------------*/
  /*----------------------------------------------*/
  submitRequest() {
    this.Form.get('name')?.setValue(this.user.name)
    this.Form.get('company')?.setValue(this.user.company)
    this.Form.get('email')?.setValue(this.user.email)
    const request: Request = this.Form.value;
    console.info('>> create/edit Entry: ', request);

    if (this.searchState === 'create') {
      console.info('>> create Entry: ', request)
      this.ptwSvc.createPTW(request).subscribe(
        (response: any) => {
          console.log('Response from server:', response);
          this.Response = JSON.stringify(response).replace(/[{\}""]/g, '');
        },
        (error: any) => {
          console.error('Error occurred:', error);
          this.Response = JSON.stringify(error.body).replace(/[{\}""]/g, '');
        }
      );
    } else if (this.searchState === 'edit' && this.idControl.value !== null) {
      console.info('>> edit Entry: ', request)
      this.ptwSvc.updatePTW(this.idControl.value, request).subscribe(
        (response: any) => {

          console.log('Response from server:', response);
          this.Response = JSON.stringify(response).replace(/[{\}""]/g, '');
        },
        (error: any) => {
          console.error('Error occurred:', error);
          this.Response = JSON.stringify(error.body).replace(/[{\}""]/g, '');
        }
      );
    }

    this.Form.reset()
    this.searchState = ""
  }


  /*----------------------------------------------*/
  /*---Search for pending. approved and pending---*/
  /*----------------------------------------------*/

  pendingSearch() {
    this.statusSearchControl.setValue("pending")

    const searchQuery: SearchQuery = this.pendingGroup.value
    console.info('>> Pending search form: ', searchQuery)
    this.pendingpermits$! = new Observable<Permit[]>((subscriber) => {
      const subscription = this.ptwSvc.searchPTW(searchQuery).subscribe(
        (permits: Permit[]) => {
          subscriber.next(permits);
          subscriber.complete();
        },
        (error: any) => {
          subscriber.error(error);
        })

      return () => {
        subscription.unsubscribe();
      }
    })
  }

  editEntry(id: number) {
    this.tabGroup.selectedIndex = 0
    this.idControl.setValue(id)
    this.onCreateEdit()
    console.info('>> edit Entry id: ', id)

  }

  cancelEntry(id: number) {
    console.info('>> cancel Entry id: ', id )
    this.ptwSvc.cancelPTW(id, this.user.name).subscribe(
      (response: any) => {
        console.log('Response from server:', response);
        // this.Response = JSON.stringify(response).replace(/[{\}""]/g, '');
      },
      (error: any) => {
        console.error('Error occurred:', error);
        // this.Response = JSON.stringify(error.body).replace(/[{\}""]/g, '');
      }
    );
    this.pendingSearch()
  }

  closeEntry(id: number) {
    console.info('>> close Entry id: ', id)
    this.ptwSvc.closePTW(id, this.user.name).subscribe(
      (response: any) => {
        console.log('Response from server:', response);
        // this.Response = JSON.stringify(response).replace(/[{\}""]/g, '');
      },
      (error: any) => {
        console.error('Error occurred:', error);
        // this.Response = JSON.stringify(error.body).replace(/[{\}""]/g, '');
      }
    );
    this.approvedSearch() 
  }

  approvedSearch() {
    this.statusSearchControl.setValue("approved")

    const searchQuery: SearchQuery = this.approvedGroup.value
    console.info('>> Approved search form: ', searchQuery)
    this.approvedpermits$! = new Observable<Permit[]>((subscriber) => {
      const subscription = this.ptwSvc.searchPTW(searchQuery).subscribe(
        (permits: Permit[]) => {
          subscriber.next(permits);
          subscriber.complete();
        },
        (error: any) => {
          subscriber.error(error);
        })

      return () => {
        subscription.unsubscribe();
      }
    })
  }



  /*-------------------------------------------------------*/
  // to provide function to change everything to lower case
  private _optionfilter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }
  private _locationfilter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.locations.filter(location => location.toLowerCase().includes(filterValue));
  }

  //formbuilder
  private createFormWithFormBuilder(): FormGroup {
    const datePipe = new DatePipe('en-US')
    const defaultDate = datePipe.transform(new Date(), 'yyyy-MM-ddTHH:mm') //default date is today
    // const defaultDate = datePipe.transform(new Date(), 'dd-MM-yyyyTHH:mm') //default date is today

    return this.fb.group({
      type: this.typeControl,
      name: this.fb.control<string>(''),
      equipment: this.fb.control<string>('', [Validators.required, Validators.minLength(3)]),
      company: this.companyControl,
      startdate: [defaultDate], //solve the reverse dates!
      enddate: [defaultDate],
      location: this.locationControl,
      comment: this.fb.control<string>('',[Validators.required, Validators.minLength(1)]),
      email: this.fb.control<string>('')
    })
  }

  private createFormSearch(): FormGroup {
    return this.fb.group({
      type: this.fb.control<string>(''),
      locations: this.fb.control<string>(''),
      status: this.statusSearchControl
    })
  }
//TODO: remove all company
  populateForm(data: Permit) {
    this.Form.patchValue({
      type: data.type,
      name: data.name,
      equipment: data.equipment,
      company: data.company,
      startdate: data.startdate,
      enddate: data.enddate,
      location: data.locations,
      comment: data.comment
    });
  }

}
