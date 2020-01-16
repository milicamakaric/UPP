import { Component, OnInit } from '@angular/core';
import { CasopisService } from '../services/casopis/casopis.service';

@Component({
  selector: 'app-casopis-admin',
  templateUrl: './casopis-admin.component.html',
  styleUrls: ['./casopis-admin.component.css']
})
export class CasopisAdminComponent implements OnInit {

  private tasks = [];
  private controls = [];

  constructor(private casopisService: CasopisService) { }

  ngOnInit() {

    let x = this.casopisService.getTasks();

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

  onSubmitForm(value, form, taskId){

    console.log('taskId: ' + taskId);

    let dto = Array();

    this.controls = form.controls;
    for(var control in this.controls){
      dto.push({fieldId: control, fieldValue: this.controls[control].value});
    }
    console.log(dto);

    this.casopisService.postAdminCasopisData(dto, taskId).subscribe(
      res => {
        console.log("res", res);
        alert('Uspesno ste pregledali podatke o casopisu.')
        window.location.href="";
    
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
