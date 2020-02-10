import { Component, OnInit } from '@angular/core';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-vreme-recenziranja',
  templateUrl: './vreme-recenziranja.component.html',
  styleUrls: ['./vreme-recenziranja.component.css']
})
export class VremeRecenziranjaComponent implements OnInit {

  private procesId = '';
  private formFieldsDto = null;
  private formFields = [];
  private controls = [];

  constructor(private radService: RadService) { 

    let x = this.radService.getVremeRecenziranjaTask();

    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.procesId = res.processInstanceId;
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

    this.radService.postVremeRecenziranjaData(dto, this.formFieldsDto.taskId).subscribe(
      res => {
        console.log("res", res);
        alert('Uspesno ste postavili vreme za recenziranje');
        window.location.href="izbor-recenzenata/" + this.formFieldsDto.processInstanceId;
    
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
