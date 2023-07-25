import { Component, OnInit, inject } from '@angular/core';
import { COMPANIES } from './_ModelandConstants/constant';
import { Router } from '@angular/router';
import { User, User_Registeration } from './_ModelandConstants/model';
import { AccountService } from './_Service/Account.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'PTWfrontend';

  actSvc: AccountService = inject(AccountService)
  router: Router = inject(Router)

  userName!: string;

  //TODO: figure out how to get the name to appear after login
  ngOnInit(): void {
    const currentUser = localStorage.getItem('currentUser');
    if (currentUser) {
      const user: User_Registeration = JSON.parse(currentUser);
      this.actSvc.updateUserValidity(true)
      this.userName = user.name

    }
  }

  getTitle(): string {
    const currentRoute = this.router.url;
    switch (currentRoute) {
      case '/login':
        return 'Login';
      case '/create':
        return 'Permit';
      case '/approval':
        return 'Dashboard';
      default:
        return '';
    }
  }

  getUserName(): string {
    return this.userName;
  }

  logout(): void {

    localStorage.clear()
    this.actSvc.updateUserValidity(false)
    location.reload()
    
  }
}
