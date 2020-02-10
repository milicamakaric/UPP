import { Component, OnInit } from '@angular/core';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-recenziranje',
  templateUrl: './recenziranje.component.html',
  styleUrls: ['./recenziranje.component.css']
})
export class RecenziranjeComponent implements OnInit {

  private procesId = '';
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private controls = [];

  constructor(private radService: RadService) {

    let x = this.radService.getRecenziranjeTask();

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
  }

  onPreuzmiRad() {
    this.radService.downloadFile(this.procesId).subscribe(
      res => {
        var blob = new Blob([res], {type: 'application/pdf'});
        var url= window.URL.createObjectURL(blob);
        window.open(url, "_blank");
      }, err => {
        alert("Error while download file");
      }
    );
  }

  onSubmitForm(value, form){

    let dto = Array();

    this.controls = form.controls;
    for(var control in this.controls){
      dto.push({fieldId: control, fieldValue: this.controls[control].value});
    }
    console.log(dto);

    this.radService.postRecenziranjeData(dto, this.formFieldsDto.taskId).subscribe(
      res => {
        console.log("res", res);
        alert('Uspesno ste obavili recenziranje.');
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
