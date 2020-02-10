import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule }   from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { ZuulPath } from './ZuulPath';
import { RegistrationComponent } from './registration/registration.component';
import { MainPageComponent } from './main-page/main-page.component';
import { ActivationComponent } from './activation/activation.component';
import { PotvrdaRecenzentiComponent } from './potvrda-recenzenti/potvrda-recenzenti.component';
import { LoginComponent } from './login/login.component';
import { JwtInterceptor } from './jwt.interceptor';
import { CasopisComponent } from './casopis/casopis.component';
import { UredniciRecenzentiComponent } from './urednici-recenzenti/urednici-recenzenti.component';
import { CasopisAdminComponent } from './casopis-admin/casopis-admin.component';
import { IspravkaComponent } from './ispravka/ispravka.component';
import { CasopisiComponent } from './casopisi/casopisi.component';
import { OdabirPlacanjaComponent } from './odabir-placanja/odabir-placanja.component';
import { BankPageComponent } from './bank-page/bank-page.component';
import { BankPath } from './BankPath';
import { CasopisPlacanjeComponent } from './casopis-placanje/casopis-placanje.component';
import { BankNewCustomerComponent } from './bank-new-customer/bank-new-customer.component';
import { BankServicePath } from './BankServicePath';
import { BitcoinNewCustomerComponent } from './bitcoin-new-customer/bitcoin-new-customer.component';
import { PaypalComponent } from './paypal/paypal.component';
import { PaypalRedComponent } from './paypal-red/paypal-red.component';
import { PaypalCancelComponent } from './paypal-cancel/paypal-cancel.component';
import { PaypalNewCustomerComponent } from './paypal-new-customer/paypal-new-customer.component';
import { CreatePlanComponent } from './create-plan/create-plan.component';
import { SubReturnComponent } from './sub-return/sub-return.component';
import { PlanCancelComponent } from './plan-cancel/plan-cancel.component';
import { RadCasopisiComponent } from './rad-casopisi/rad-casopisi.component';
import { PlacanjeComponent } from './placanje/placanje.component';
import { RadComponent } from './rad/rad.component';
import { KoautorComponent } from './koautor/koautor.component';
import { RadPregledComponent } from './rad-pregled/rad-pregled.component';
import { RadPdfComponent } from './rad-pdf/rad-pdf.component';
import { PdfIspravkaComponent } from './pdf-ispravka/pdf-ispravka.component';
import { IzborRecenzenataComponent } from './izbor-recenzenata/izbor-recenzenata.component';
import { VremeRecenziranjaComponent } from './vreme-recenziranja/vreme-recenziranja.component';
import { RecenziranjeComponent } from './recenziranje/recenziranje.component';
import { NoviRecenzentiComponent } from './novi-recenzenti/novi-recenzenti.component';
import { AnalizaRecenzijaComponent } from './analiza-recenzija/analiza-recenzija.component';
import { IspravkaRadComponent } from './ispravka-rad/ispravka-rad.component';
import { PregledRadaComponent } from './pregled-rada/pregled-rada.component';

const Routes = [
  { path: "", component: MainPageComponent },
  { path: "registration", component: RegistrationComponent },
  { path: "activation/:id_user/:id_process", component: ActivationComponent },
  { path: "recenzenti", component: PotvrdaRecenzentiComponent },
  { path: "login", component: LoginComponent },
  { path: "casopis", component: CasopisComponent },
  { path: "urednici-recenzenti/:id_process", component: UredniciRecenzentiComponent },
  { path: "casopis-admin", component: CasopisAdminComponent },
  { path: "ispravka", component: IspravkaComponent },
  { path: "casopisi", component: CasopisiComponent },
  { path: "odabir_placanja/:id_casopis/:id_rad", component: OdabirPlacanjaComponent},
  { path: "bank-page/:id_payment", component: BankPageComponent },
  { path: "casopis-placanje/:id_casopis", component: CasopisPlacanjeComponent },
  { path: "bank-new-customer/:id_customer", component: BankNewCustomerComponent },
  { path: "paypal-new-customer/:id_customer", component:PaypalNewCustomerComponent},
  { path: "bitcoin-new-customer/:id_customer", component:BitcoinNewCustomerComponent},
  { path: 'paypal', component: PaypalComponent},
  { path: 'paypal/red/:rad_id', component: PaypalRedComponent},
  { path: 'paypal/cancel', component: PaypalCancelComponent},
  { path: 'sub/return/:sellerId', component: SubReturnComponent},
  { path: 'sub/cancel', component: PlanCancelComponent},
  { path: 'plan/create/:sellerId', component: CreatePlanComponent},
  { path: 'rad-casopisi', component: RadCasopisiComponent},
  { path: 'placanje/:id_potproces/:id_proces', component: PlacanjeComponent},
  { path: 'rad/:id_proces', component: RadComponent},
  { path: 'koautor/:id_proces', component: KoautorComponent},
  { path: 'rad-pregled', component: RadPregledComponent},
  { path: 'rad-pdf/:id_rad/:id_proces', component: RadPdfComponent},
  { path: 'pdf-ispravka', component: PdfIspravkaComponent},
  { path: 'vreme-recenziranja', component: VremeRecenziranjaComponent},
  { path: 'izbor-recenzenata/:id_proces', component: IzborRecenzenataComponent},
  { path: 'recenziranje', component: RecenziranjeComponent },
  { path: 'novi-recenzenti', component: NoviRecenzentiComponent },
  { path: 'analiza-recenzija', component: AnalizaRecenzijaComponent },
  { path: 'ispravka-rad', component: IspravkaRadComponent },
  { path: 'pregled-rada', component: PregledRadaComponent }
]

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    MainPageComponent,
    ActivationComponent,
    PotvrdaRecenzentiComponent,
    LoginComponent,
    CasopisComponent,
    UredniciRecenzentiComponent,
    CasopisAdminComponent,
    IspravkaComponent,
    CasopisiComponent,
    OdabirPlacanjaComponent,
    BankPageComponent,
    CasopisPlacanjeComponent,
    BankNewCustomerComponent,
    BitcoinNewCustomerComponent,
    PaypalComponent,
    PaypalRedComponent,
    PaypalCancelComponent,
    PaypalNewCustomerComponent,
    CreatePlanComponent,
    SubReturnComponent,
    PlanCancelComponent,
    RadCasopisiComponent,
    PlacanjeComponent,
    RadComponent,
    KoautorComponent,
    RadPregledComponent,
    RadPdfComponent,
    PdfIspravkaComponent,
    IzborRecenzenataComponent,
    VremeRecenziranjaComponent,
    RecenziranjeComponent,
    NoviRecenzentiComponent,
    AnalizaRecenzijaComponent,
    IspravkaRadComponent,
    PregledRadaComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule.forRoot(Routes),
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }, ZuulPath, BankPath],
  bootstrap: [AppComponent]
})
export class AppModule { }
