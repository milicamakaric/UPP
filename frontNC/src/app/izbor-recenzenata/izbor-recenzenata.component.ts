import { Component, OnInit } from '@angular/core';
import { RadService } from '../services/rad/rad.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-izbor-recenzenata',
  templateUrl: './izbor-recenzenata.component.html',
  styleUrls: ['./izbor-recenzenata.component.css']
})
export class IzborRecenzenataComponent implements OnInit {

  private procesId = '';
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private error = false;
  private recenzentiForm: FormGroup;
  private select_box1: FormControl;

  constructor(private route: ActivatedRoute, private radService: RadService) {

    this.route.params.subscribe( params => {
      this.procesId = params.id_proces;
    });

    let x = this.radService.getIzborRecenzenataTask();

    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.procesId = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum'){
            this.enumValues = Object.keys(field.type.values);
          }
        });
      },
      err => {
        console.log("Error occured");
      }
    );

   }

   ngOnInit() {
    this.createFormControls();
    this.createForm();
  }

  createFormControls(){
    this.select_box1 = new FormControl('', Validators.required);
    
  }
  createForm(){
    this.recenzentiForm = new FormGroup({
      select_box1: this.select_box1
    });
    
  }

  onSubmitRecenzentiForm(value, form){

    this.error = false;

    let o = new Array();
    for (var property in value) {

      if(property == 'select_box1' && value[property].length<2){
        console.log('odabrano manje od 2 recenzenta');
        this.error = true;
      }
    
    }

    if(this.error == false){

      for(var property in value){
        for(var pom of value[property]){
          console.log(property, ': ', pom);
          o.push({fieldId : property, fieldValue : pom});
        }
      }

      console.log(o);

        this.radService.postIzborRecenenataData(o, this.formFieldsDto.taskId).subscribe(
        res => {
          console.log("res", res);
          alert('Uspesno ste odabrali recenzente');
          window.location.href="";
      
        },
        err => {
          console.log("Error occured");
        }
      );

    }
  }

  nazad(){
    window.location.href='';
  }
}
