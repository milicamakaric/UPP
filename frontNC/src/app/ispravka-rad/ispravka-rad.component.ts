import { Component, OnInit } from '@angular/core';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-ispravka-rad',
  templateUrl: './ispravka-rad.component.html',
  styleUrls: ['./ispravka-rad.component.css']
})
export class IspravkaRadComponent implements OnInit {

  private procesId = '';
  private formFieldsDto = null;
  private formFields = [];
  private controls = [];
  private enumValues = [];
  private fileUrl: string;
  private fileToUpload: File;

  constructor(private radService: RadService) {

    let x = this.radService.getIspravkaAutorTask();

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

  handleFileInput(file:FileList){
    this.fileToUpload =file.item(0);
    var reader = new FileReader();
    reader.onload=(event:any)=>{
      this.fileUrl =event.target.result;
    }
    reader.readAsDataURL(this.fileToUpload);
    console.log("URL " + this.fileUrl);
    console.log("file " + this.fileToUpload);
  }

  onSubmitForm(value, form){

    let dto = Array();

    this.controls = form.controls;
    for(var control in this.controls){
      dto.push({fieldId: control, fieldValue: this.controls[control].value});
    }
    console.log(dto);

    this.radService.postIspravkaAutorData(dto, this.formFieldsDto.taskId).subscribe(
      res => {
        console.log("res", res);

        const formData = new FormData();  
        formData.append("file", this.fileToUpload);

        this.radService.postFile(this.procesId, formData).subscribe(
          data => {
            alert('Uspesno ste obavili ispravku rada.');
            window.location.href="";
          }, error => {
            alert("error uploading file");
          }
        );
        
    
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
