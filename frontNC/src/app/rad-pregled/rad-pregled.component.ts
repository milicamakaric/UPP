import { Component, OnInit } from '@angular/core';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-rad-pregled',
  templateUrl: './rad-pregled.component.html',
  styleUrls: ['./rad-pregled.component.css']
})
export class RadPregledComponent implements OnInit {

  private tasks = [];
  private controls = [];

  constructor(private radService: RadService) { }

  ngOnInit() {

    let x = this.radService.getGlavniUrednikTasks();

    x.subscribe(
      res => {
        console.log('res:', res);
        this.tasks = res;
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmitForm(value, form, taskId, processInstanceId){

    console.log('taskId: ' + taskId);

    let dto = Array();

    this.controls = form.controls;
    for(var control in this.controls){
      dto.push({fieldId: control, fieldValue: this.controls[control].value});
    }
    console.log(dto);

    this.radService.postGlavniUrednikData(dto, taskId).subscribe(
      res => {
        console.log("res", res);
        if(res.status == 'relevantan'){
          window.location.href = 'rad-pdf/' + res.message + '/' + processInstanceId;
        }else{
          alert('Uspesno ste odbili objavljivanje rada.');
          window.location.href="";
        }
    
      },
      err => {
        console.log("Error occured");
      }
    );

  }

  nazad(){
    window.location.href = "";
  }

}
