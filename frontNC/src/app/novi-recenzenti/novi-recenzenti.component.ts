import { Component, OnInit } from '@angular/core';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-novi-recenzenti',
  templateUrl: './novi-recenzenti.component.html',
  styleUrls: ['./novi-recenzenti.component.css']
})
export class NoviRecenzentiComponent implements OnInit {

  private enumValues = [];
  private tasks = [];
  private controls = [];

  constructor(private radService: RadService) { }

  ngOnInit() {

    this.radService.getNoviRecenzentiTasks();
    let x = this.radService.getNoviRecenzentiTasks();

    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
        this.tasks.forEach( (task) => {
          task.formFields.forEach( (field) => {
            if(field.type.name=='enum'){
              this.enumValues = Object.keys(field.type.values);
            }
          });
        });
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmitForm(value, form, taskId){

    console.log('taskId: ' + taskId);

    let dto = Array();
    this.controls = form.controls;
     
    for(var control in this.controls){
      dto.push({fieldId : control, fieldValue : this.controls[control].value});
      
    }
    console.log(dto);

    this.radService.postNoviRecenentData(dto, taskId).subscribe(
      res => {
        console.log("res", res);
        alert('Uspesno ste odabrali novog recenzenta');
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
