import { NgModule } from "@angular/core";

import { MatToolbarModule } from '@angular/material/toolbar'
import {MatIconRegistry, MatIconModule } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button'
import { MatInputModule } from '@angular/material/input'
import { MatFormFieldModule } from '@angular/material/form-field'
import {MatSelectModule} from '@angular/material/select';
import {MatMenuModule} from '@angular/material/menu';
import {MatTabsModule} from '@angular/material/tabs';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatCardModule} from '@angular/material/card';
import {MatDividerModule} from '@angular/material/divider';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatExpansionModule} from '@angular/material/expansion'
import {MatGridListModule} from '@angular/material/grid-list';

const matModules: any[] = [
  MatToolbarModule, MatButtonModule, MatIconModule,
  MatInputModule, MatFormFieldModule,MatSelectModule, MatMenuModule, 
  MatTabsModule, MatSidenavModule, MatAutocompleteModule,
  MatCardModule, MatDividerModule, MatTooltipModule,
  MatCheckboxModule, MatSlideToggleModule, MatExpansionModule,
  MatGridListModule
]



@NgModule({
  imports: matModules,
  exports: matModules
})
export class MaterialModule {  
  }

