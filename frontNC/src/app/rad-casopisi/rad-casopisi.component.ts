import { Component, OnInit } from '@angular/core';
import { KpService } from '../services/kp/kp.service';
import { RadService } from '../services/rad/rad.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-rad-casopisi',
  templateUrl: './rad-casopisi.component.html',
  styleUrls: ['./rad-casopisi.component.css']
})
export class RadCasopisiComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstance = "";
  private enumValues = [];
  private radCasopisForm: FormGroup;
  private izabrani_casopis: FormControl;

  constructor(private kpService: KpService, private radService: RadService) { 

    this.createFormControls();
    this.createForm();

    radService.startProcess().subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum' && field.id=='izabrani_casopis'){
            this.enumValues = Object.keys(field.type.values);
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                // console.log('jeste polje required');
                this.izabrani_casopis.setValidators(Validators.required);
                this.izabrani_casopis.updateValueAndValidity();
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

  ngOnInit() {}

  createFormControls(){
    this.izabrani_casopis = new FormControl('');
    
  }

  createForm(){
    this.radCasopisForm = new FormGroup({
      izabrani_casopis: this.izabrani_casopis
    });
  }

  onSubmitRadCasopisForm(value, form){

    let o = new Array();
    for (var property in value) {
      console.log(property, ': ', value[property]);
      o.push({fieldId : property, fieldValue : value[property]});
    
    }

    console.log(o);

    this.radService.postIzborCasopisa(o, this.formFieldsDto.taskId).subscribe(
      res => {
        console.log("res", res);
        if(res.status == 'true'){
          console.log('placanje...');
          window.location.href = 'placanje/' + res.message + '/' + this.processInstance;
        }else{
          console.log('unos informacija o radu...')
          window.location.href = 'rad/' + this.processInstance;
        }
    
      },
      err => {
        console.log("Error occured");
      }
    );

  }

  nazad(){
    window.location.href='';
  }

}
