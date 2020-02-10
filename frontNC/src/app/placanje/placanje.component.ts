import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RadService } from '../services/rad/rad.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-placanje',
  templateUrl: './placanje.component.html',
  styleUrls: ['./placanje.component.css']
})
export class PlacanjeComponent implements OnInit {

  private procesId = '';
  private potprocesId = ';'
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private placanjeForm: FormGroup;
  private nacin_placanja: FormControl;
  private iznos: FormControl;

  constructor(private route: ActivatedRoute, private radService: RadService) {

    this.route.params.subscribe( params => {
      this.procesId = params.id_proces;
      this.potprocesId = params.id_potproces;
    });

    this.createFormControls();
    this.createForm();

    radService.placanje(this.potprocesId).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum' && field.id=='nacin_placanja'){
            this.enumValues = Object.keys(field.type.values);
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.nacin_placanja.setValidators(Validators.required);
                this.nacin_placanja.updateValueAndValidity();
              }
            }
          }else{
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.iznos.setValidators(Validators.required);
                this.iznos.updateValueAndValidity();
              }
            }
          }
        });

      },
      err => {
        console.log("Error occured");
      }
    );

   }

  ngOnInit() {
  }

  createFormControls(){
    this.nacin_placanja = new FormControl('');
    this.iznos = new FormControl('');
    
  }

  createForm(){
    this.placanjeForm = new FormGroup({
      nacin_placanja: this.nacin_placanja,
      iznos: this.iznos
    });
  }

  onSubmitPlacanjeForm(value, form){

    console.log('submitting...')

    let o = new Array();
    for (var property in value) {
      console.log(property, ': ', value[property]);
      o.push({fieldId : property, fieldValue : value[property]});
    
    }

    console.log(o);

    this.radService.postPlacanje(o, this.formFieldsDto.taskId).subscribe(
      res => {
        console.log("res", res);
        window.location.href = 'rad/' + this.procesId;
    
      },
      err => {
        console.log("Error occured");
      }
    );

  }

}
