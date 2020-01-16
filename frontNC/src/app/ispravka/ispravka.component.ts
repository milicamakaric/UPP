import { Component, OnInit } from '@angular/core';
import { CasopisService } from '../services/casopis/casopis.service';

@Component({
  selector: 'app-ispravka',
  templateUrl: './ispravka.component.html',
  styleUrls: ['./ispravka.component.css']
})
export class IspravkaComponent implements OnInit {

  private tasks = [];
  private controls = [];
  private formFields = [];
  private enumValues = [];

  constructor(private casopisService: CasopisService) { }

  ngOnInit() {

    let x = this.casopisService.getUrednikTasks();

    x.subscribe(
      res => {
        console.log('res:', res);
        this.tasks = res;
        this.formFields = res.formFields;
        this.tasks.forEach( (task) => {
          task.formFields.forEach( (field) => {
            if( field.type.name=='enum'){
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
      dto.push({fieldId: control, fieldValue: this.controls[control].value});
    }
    console.log(dto);

    this.casopisService.postIspravkaData(dto, taskId).subscribe(
      res => {
        console.log("res", res);
        alert('Uspesno ste poslali ispravljene podatke o casopisu.')
        window.location.href="ispravka";
    
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
