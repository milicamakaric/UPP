import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-koautor',
  templateUrl: './koautor.component.html',
  styleUrls: ['./koautor.component.css']
})
export class KoautorComponent implements OnInit {

  private procesId = '';
  private formFieldsDto = null;
  private formFields = [];
  private koautorForm: FormGroup;
  private koautor_ime: FormControl;
  private koautor_email: FormControl;
  private koautor_grad: FormControl;
  private koautor_drzava: FormControl;

  constructor(private route: ActivatedRoute, private radService: RadService) {

    this.route.params.subscribe( params => {
      this.procesId = params.id_proces;
    });

    this.createFormControls();
    this.createForm();

    radService.koautor(this.procesId).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) =>{
          
          if(field.id=='koautor_ime'){
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.koautor_ime.setValidators(Validators.required);
                this.koautor_ime.updateValueAndValidity();
              }
            }
          }else if(field.id=='koautor_email'){
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.koautor_email.setValidators(Validators.required);
                this.koautor_email.updateValueAndValidity();
              }
            }
          }else if(field.id=='koautor_grad'){
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.koautor_grad.setValidators(Validators.required);
                this.koautor_grad.updateValueAndValidity();
              }
            }
          }else if(field.id=='koautor_drzava'){
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.koautor_drzava.setValidators(Validators.required);
                this.koautor_drzava.updateValueAndValidity();
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
    this.koautor_ime = new FormControl('');
    this.koautor_email = new FormControl('');
    this.koautor_grad = new FormControl('');
    this.koautor_drzava = new FormControl('');
    
  }

  createForm(){
    this.koautorForm = new FormGroup({
      koautor_ime: this.koautor_ime,
      koautor_email: this.koautor_email,
      koautor_grad: this.koautor_grad,
      koautor_drzava: this.koautor_drzava
    });
  }

  onSubmitKoautorForm(value, form){

    console.log('submitting...')

    let o = new Array();
    for (var property in value) {
      // console.log(property, ': ', value[property]);
      o.push({fieldId : property, fieldValue : value[property]});
    
    }

    console.log(o);

    this.radService.postKoautorData(o, this.formFieldsDto.taskId).subscribe(
      res => {
        console.log("res", res);
        if(res.status == 'ima'){
          console.log('ima jos koautora');
          window.location.href='koautor/' + this.procesId;
        }else{
          // TODO redirekcija
          console.log('nema vise koautora');
          window.location.href = '';
        }
    
      },
      err => {
        console.log("Error occured");
      }
    );

  }

}
