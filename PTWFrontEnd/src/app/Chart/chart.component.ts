import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ChartService } from '../_Service/Chart.service';
import { Router } from '@angular/router';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { COLD_ICON, CONFINED_ICON, HOT_ICON, ONGOING_ICON, PENDING_ICON } from '../_ModelandConstants/iconConstant';

//SVG link
const hot = HOT_ICON
const cold = COLD_ICON
const confined = CONFINED_ICON
const pending = PENDING_ICON
const ongoing = ONGOING_ICON


@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})

export class ChartComponent implements OnInit {

  chartSvc = inject(ChartService)
  router = inject(Router)


  //Chart formGroup for search
  ChartForm!: FormGroup
  locationControl = new FormControl<String>('')
  typeArray!: FormArray


  DonutURL: string = ''
  ChartURL: string = ''

  permitpending!: String
  permitapproved!: String
  permitclosed!: String
  permitcancel!: String

  fb: FormBuilder = inject(FormBuilder)
  iconRegistry: MatIconRegistry = inject(MatIconRegistry)
  sanitizer: DomSanitizer = inject(DomSanitizer)


  ngOnInit(): void {
    this.chartSvc.getInfoType("all").subscribe(
      (response: any) => {
        console.log('Response from chart:', response);
        this.DonutURL = this.chartSvc.generateURL(response)
      },
      (error: any) => {
        console.error('Error occurred during chart:', error);
      }),

      this.chartSvc.getInfoLocation("all").subscribe(
        (response: any) => {
          console.log('Response from chart:', response);
          this.ChartURL = this.chartSvc.generateChartURL(response)
        },
        (error: any) => {
          console.error('Error occurred during chart:', error);
        }),

      // get permit number

      this.chartSvc.getpermitNumbers().subscribe(
        (response: any) => {
          console.log('Response from chart:', response)
          this.permitpending = response.pending
          this.permitapproved = response.approved
          this.permitclosed = response.closed
          this.permitcancel = response.cancel
        },
        (error: any) => {
          console.error('Error occurred during chart:', error);
        }),

      //linking the svg file to the respective tag in html
    this.iconRegistry.addSvgIconLiteral('hot', this.sanitizer.bypassSecurityTrustHtml(hot))
    this.iconRegistry.addSvgIconLiteral('cold', this.sanitizer.bypassSecurityTrustHtml(cold))
    this.iconRegistry.addSvgIconLiteral('confined', this.sanitizer.bypassSecurityTrustHtml(confined))
    this.iconRegistry.addSvgIconLiteral('pending', this.sanitizer.bypassSecurityTrustHtml(pending))
    this.iconRegistry.addSvgIconLiteral('ongoing', this.sanitizer.bypassSecurityTrustHtml(ongoing))
  }

  gotoEdit() {
    this.router.navigate(['/create']);
  }

}
