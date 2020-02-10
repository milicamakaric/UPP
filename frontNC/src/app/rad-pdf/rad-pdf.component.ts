import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RadService } from '../services/rad/rad.service';
import { FormGroup, Validators, FormControl } from '@angular/forms';

@Component({
  selector: 'app-rad-pdf',
  templateUrl: './rad-pdf.component.html',
  styleUrls: ['./rad-pdf.component.css']
})
export class RadPdfComponent implements OnInit {

  private radId = '';
  private procesId = '';
  private formFieldsDto = null;
  private formFields = [];
  private controls = [];

  constructor(private route: ActivatedRoute, private radService: RadService) {

    this.route.params.subscribe( params => {
      this.radId = params.id_rad;
      this.procesId = params.id_proces;
    });


    radService.getRadPdf(this.procesId).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;

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

    this.radService.postPregledPdfData(dto, this.formFieldsDto.taskId).subscribe(
      res => {
        console.log("res", res);
        alert('Uspesno ste pregledali podatke o radu.')
        window.location.href='';
    
      },
      err => {
        console.log("Error occured");
      }
    );

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

}
