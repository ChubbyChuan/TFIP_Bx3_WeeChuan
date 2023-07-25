import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User, User_Registeration } from '../_ModelandConstants/model';
import { AccountService } from '../_Service/Account.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;

  fb: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)
  actSvc: AccountService = inject(AccountService)

  isUserValid: Boolean = false
  user!: User_Registeration

  ngOnInit(): void {
    this.loginForm = this.createLoginForm()
    const currentUser = localStorage.getItem('currentUser')
    if (currentUser) {
      this.user = JSON.parse(currentUser)
      this.router.navigate(['/approval']);
    }

  }

  get formControls() {
    return this.loginForm.controls;
  }

  onSubmit() {
    const user: User = this.loginForm.value;
    console.info('>> Login info: ', user);

    this.actSvc.verifyUser(user).subscribe(
      (response: User_Registeration) => {
        console.log('Response from server: ', response);
        if (response) {
          this.actSvc.updateUserValidity(true); // Update user validity

          localStorage.setItem('currentUser', JSON.stringify(response));
          this.router.navigate(['/approval']);
        }
      },
      (error: any) => {
        alert('Invalid credentials');
        console.error('Error occurred:', error);

      }
    );
  }

  invalidForm() {
    return this.loginForm.invalid
  }

  private createLoginForm(): FormGroup {
    return this.fb.group({
      email: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(3)])
    })
  }
}