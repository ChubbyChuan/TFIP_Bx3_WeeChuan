import { Component, OnInit, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User, User_Registeration } from '../_ModelandConstants/model';
import { AccountService } from '../_Service/Account.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;

  fb: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)
  actSvc: AccountService = inject(AccountService)

  isUserValid: Boolean = true


  ngOnInit(): void {
    this.loginForm = this.createRegisterForm()
  }

  onSubmit() {
    const user: User_Registeration = this.loginForm.value;
    console.info('>> Login info: ', user)

    this.actSvc.registerUser(user).subscribe(
      (response: any) => {
        console.log('Response from server: ', response)
      },
      (error: any) => {
        console.error('Error occurred:', error)
      }
    )
  } 


  invalidForm() {
    return this.loginForm.invalid
  }

  private createRegisterForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required, Validators.minLength(3)]),
      email: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(3)]),
      company: this.fb.control('', [Validators.required, Validators.minLength(3)])
    })
  }
}