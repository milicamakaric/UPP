import { Component, OnInit } from '@angular/core';
import { RadService } from '../services/rad/rad.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SearchService } from '../services/search/search.service';

@Component({
  selector: 'app-izbor-recenzenata',
  templateUrl: './izbor-recenzenata.component.html',
  styleUrls: ['./izbor-recenzenata.component.css']
})
export class IzborRecenzenataComponent implements OnInit {

  private procesId = '';
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private error = false;
  private recenzentiForm: FormGroup;
  private select_box1: FormControl;
  private recenzenti = [];
  private noRecenzent = false;

  private disabledAll = false;
  private disabledNaucneOblasti = false;
  private disabledMLT = false;
  private disabledGeo = false;

  constructor(private route: ActivatedRoute, private radService: RadService, private searchService: SearchService) {

    this.route.params.subscribe( params => {
      this.procesId = params.id_proces;
    });

    this.radService.getIzborRecenzenataTask().subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.procesId = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          
        if( field.type.name=='enum'){
          this.enumValues = Object.keys(field.type.values);
        }

        this.allRecenzenti();

        });
      },
      err => {
        console.log("Error occured");
      }
    );

   }

   ngOnInit() {
    this.createFormControls();
    this.createForm();
  }

  createFormControls(){
    this.select_box1 = new FormControl('', Validators.required);
    
  }
  createForm(){
    this.recenzentiForm = new FormGroup({
      select_box1: this.select_box1
    });
    
  }

  allRecenzenti(){

    this.noRecenzent = false;
    this.error = false;

    this.searchService.searchAll(this.procesId).subscribe(
      res=>{
        console.log(res);
        this.recenzenti = res;

        this.disabledAll = true;
        this.disabledNaucneOblasti = false;
        this.disabledMLT = false;
        this.disabledGeo = false;

      },
      error=>{
        console.log(error);
      }
    )
  }

  byNaucneOblasti(){

    this.noRecenzent = false;
    this.error = false;

    this.searchService.searchByNaucneOblasti(this.procesId).subscribe(
      res=>{
        console.log(res);
        this.recenzenti = res;

        this.disabledAll = false;
        this.disabledNaucneOblasti = true;
        this.disabledMLT = false;
        this.disabledGeo = false;

        if(this.recenzenti.length == 0){
          this.noRecenzent = true;
        }
      },
      error=>{
        console.log(error);
      }
    )
  }

  byMoreLikeThis(){

    this.noRecenzent = false;
    this.error = false;

    this.searchService.searchByMoreLikeThis(this.procesId).subscribe(
      res=>{
        console.log(res);
        this.recenzenti = res;

        this.disabledAll = false;
        this.disabledNaucneOblasti = false;
        this.disabledMLT = true;
        this.disabledGeo = false;
        
        if(this.recenzenti.length == 0){
          this.noRecenzent = true;
        }
      },
      error=>{
        console.log(error);
      }
    )
    
  }

  byGeoSpace(){

    this.noRecenzent = false;
    this.error = false;

    this.searchService.searchByGeoSpacing(this.procesId).subscribe(
      res=>{
        console.log(res);
        this.recenzenti = res;

        this.disabledAll = false;
        this.disabledNaucneOblasti = false;
        this.disabledMLT = false;
        this.disabledGeo = true;

        if(this.recenzenti.length == 0){
          this.noRecenzent = true;
        }
      },
      error=>{
        console.log(error);
      }
    );
  }

  onSubmitRecenzentiForm(value, form){

    this.error = false;

    let o = new Array();
    for (var property in value) {

      if(property == 'select_box1' && value[property].length<2){
        console.log('odabrano manje od 2 recenzenta');
        this.error = true;
      }
    
    }

    if(this.error == false){

      for(var property in value){
        for(var pom of value[property]){
          console.log(property, ': ', pom);
          o.push({fieldId : property, fieldValue : pom});
        }
      }

      console.log(o);

        this.radService.postIzborRecenenataData(o, this.formFieldsDto.taskId).subscribe(
        res => {
          console.log("res", res);
          alert('Uspesno ste odabrali recenzente');
          window.location.href="";
      
        },
        err => {
          console.log("Error occured");
        }
      );

    }
  }

  nazad(){
    window.location.href='';
  }
}
