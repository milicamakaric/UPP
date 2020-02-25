import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-rad',
  templateUrl: './rad.component.html',
  styleUrls: ['./rad.component.css']
})
export class RadComponent implements OnInit {

  private procesId = '';
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private radForm: FormGroup;
  private naslov: FormControl;
  private kljucni_pojmovi: FormControl;
  private apstrakt: FormControl;
  private naucna_oblast: FormControl;
  private pdf: FormControl;
  private koautori_broj: FormControl;
  private fileUrl: string;
  private fileToUpload: File;

  constructor(private route: ActivatedRoute, private radService: RadService) {

    this.route.params.subscribe( params => {
      this.procesId = params.id_proces;
    });

    this.createFormControls();
    this.createForm();

    radService.radInfo(this.procesId).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum' && field.id=='naucna_oblast'){
            this.enumValues = Object.keys(field.type.values);
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.naucna_oblast.setValidators(Validators.required);
                this.naucna_oblast.updateValueAndValidity();
              }
            }
          }else if(field.id=='naslov'){
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.naslov.setValidators(Validators.required);
                this.naslov.updateValueAndValidity();
              }
            }
          }else if(field.id=='kljucni_pojmovi'){
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.kljucni_pojmovi.setValidators(Validators.required);
                this.kljucni_pojmovi.updateValueAndValidity();
              }
            }
          }else if(field.id=='apstrakt'){
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.apstrakt.setValidators(Validators.required);
                this.apstrakt.updateValueAndValidity();
              }
            }
          }else if(field.id=='pdf'){
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.pdf.setValidators(Validators.required);
                this.pdf.updateValueAndValidity();
              }
            }
          }else if(field.id=='koautori_broj'){
            for(var validator of field.validationConstraints ){
              if(validator.name == 'required'){
                this.koautori_broj.setValidators(Validators.required);
                this.koautori_broj.updateValueAndValidity();
              }
            }
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

  createFormControls(){
    this.naslov = new FormControl('');
    this.kljucni_pojmovi = new FormControl('');
    this.apstrakt = new FormControl('');
    this.naucna_oblast = new FormControl('');
    this.pdf = new FormControl('');
    this.koautori_broj = new FormControl('');
    
  }

  createForm(){
    this.radForm = new FormGroup({
      naslov: this.naslov,
      kljucni_pojmovi: this.kljucni_pojmovi,
      apstrakt: this.apstrakt,
      naucna_oblast: this.naucna_oblast,
      pdf: this.pdf,
      koautori_broj: this.koautori_broj
    });
  }

  handleFileInput(file:FileList){
    this.fileToUpload =file.item(0);
    var reader = new FileReader();
    reader.onload=(event:any)=>{
      this.fileUrl =event.target.result;
    }
    reader.readAsDataURL(this.fileToUpload);
    console.log("URL " + this.fileUrl);
    console.log(this.fileToUpload);
  }

  onSubmitRadForm(value, form){

    console.log('submitting...')

    let o = new Array();
    for (var property in value) {
      // console.log(property, ': ', value[property]);
      o.push({fieldId : property, fieldValue : value[property]});
    
    }

    console.log(o);

    this.radService.postRadInfo(o, this.formFieldsDto.taskId).subscribe(
      res => {
        console.log("res", res);
        
        const formData = new FormData();  
        formData.append("file", this.fileToUpload);

        this.radService.postFile(this.procesId, formData).subscribe(
          data => {
            window.location.href='koautor/' + this.procesId;
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

}
