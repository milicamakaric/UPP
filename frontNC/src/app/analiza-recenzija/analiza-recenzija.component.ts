import { Component, OnInit } from '@angular/core';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-analiza-recenzija',
  templateUrl: './analiza-recenzija.component.html',
  styleUrls: ['./analiza-recenzija.component.css']
})
export class AnalizaRecenzijaComponent implements OnInit {

  private procesId = '';
  private formFieldsDto = null;
  private formFields = [];
  private enumValuesKomentarAutoru = [];
  private enumValuesKomentarUredniku = [];
  private enumValuesPreporuka = [];
  private enumValuesOdluka = [];
  private controls = [];

  constructor(private radService: RadService) {

    let x = this.radService.getAnalizaRecenzijaTask();

    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.procesId = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum' && field.id=='komentar_autoru_analiza'){
            this.enumValuesKomentarAutoru = Object.keys(field.type.values);
            // console.log(this.enumValuesKomentarAutoru);
          }
          else if( field.type.name=='enum' && field.id=='komentar_uredniku_analiza'){
            this.enumValuesKomentarUredniku = Object.keys(field.type.values);
            // console.log(this.enumValuesKomentarUredniku);
          }
          else if( field.type.name=='enum' && field.id=='preporuka_analiza'){
            this.enumValuesPreporuka = Object.keys(field.type.values);
            // console.log(this.enumValuesPreporuka);
          }
          else if( field.type.name=='enum' && field.id=='odluka'){
            this.enumValuesOdluka = Object.keys(field.type.values);
            // console.log(this.enumValuesOdluka);
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

  onSubmitForm(value, form){

    let dto = Array();

    this.controls = form.controls;
    for(var control in this.controls){
      dto.push({fieldId: control, fieldValue: this.controls[control].value});
    }
    console.log(dto);

    this.radService.postAnalizaRecenzijaData(dto, this.formFieldsDto.taskId).subscribe(
      res => {
        console.log("res", res);
        alert('Uspesno ste obavili analizu recenzija.');
        window.location.href="";
    
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
