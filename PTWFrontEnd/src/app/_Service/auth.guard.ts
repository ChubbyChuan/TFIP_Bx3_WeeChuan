import { Inject, Injectable, inject } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { AccountService } from "./Account.service";
import { User } from "../_ModelandConstants/model";

import { Observable } from "rxjs/internal/Observable";
import { map } from "rxjs";

@Injectable()
export class AuthGuard implements CanActivate {
    router = inject(Router)
    actSvc: AccountService = inject(AccountService)

    canActivate(): boolean {
      const isLoggedIn = this.actSvc.isUserValid;
      if (isLoggedIn) {
        return true;
      } else {
        this.router.navigate(['/login']);
        return false;
      }
    }
}
