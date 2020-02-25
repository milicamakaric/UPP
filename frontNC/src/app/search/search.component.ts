import { Component, OnInit } from '@angular/core';
import { SearchService } from '../services/search/search.service';
import { ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl } from '@angular/forms';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  private tipPretrage = 0;
  private pretragaForm: FormGroup;
  private prikaziRezultate = false;
  private radovi = [];
  private naucneOblastiList = [];

  private frazaOznacena = false;
  private nazivCasopisa = "";
  private naslovRada = "";
  private autori = "";
  private kljucniPojmovi = "";
  private naucneOblasti = "";
  private sadrzaj = "";

  constructor(private searchService: SearchService, private route :ActivatedRoute, private radService: RadService) {

    this.route.params.subscribe( params => {
      this.tipPretrage = params.type; 
    });

    this.prikaziRezultate = false;

   }

  ngOnInit() {

    this.createForm();
    this.prikaziRezultate = false;

    this.searchService.getNaucneOblasti().subscribe(
      res=>{
        console.log(res);
        this.naucneOblastiList = res;
      },
      error=>{
        console.log(error);
      }
    );

  }

  createForm(){
    this.pretragaForm = new FormGroup({
      nazivCasopisa: new FormControl(''),
      naslovRada: new FormControl(''),
      autori: new FormControl(''),
      kljucniPojmovi: new FormControl(''),
      naucneOblasti: new FormControl('')
    });
  }

  back(){
    this.prikaziRezultate = false;
  }

  nazad(){
    window.location.href = '';
  }

  preuzmiRad(id) {

    console.log('preuzmi rad id: ' + id);

    this.radService.downloadRad(id).subscribe(
      res => {
        var blob = new Blob([res], {type: 'application/pdf'});
        var url= window.URL.createObjectURL(blob);
        window.open(url, "_blank");
      }, err => {
        alert("Error while download file");
      }
    );
  }

  kupiRad(radId){
    console.log('kupi rad id: ' + radId);

    this.radService.kupiRad(radId).subscribe(
      res => {
        alert('Uspesno ste kupili rad');
        window.location.href = 'search/' + this.tipPretrage;
      }, err => {
        alert("Error");
      }
    );
  }

  search(){
    if(this.tipPretrage == 1){

      let fraza = 0;
      if(this.frazaOznacena){
       fraza = 1; 
      }

      this.searchService.search(this.nazivCasopisa, fraza, 'naslovCasopisa').subscribe(
        res=>{
          console.log(res);
          this.radovi = res;
          this.prikaziRezultate = true;
        },
        error=>{
          console.log(error);
        }
      )

    }else if(this.tipPretrage == 2){

      let fraza = 0;
      if(this.frazaOznacena){
       fraza = 1; 
      }

      this.searchService.search(this.naslovRada, fraza, 'naslov').subscribe(
        res=>{
          console.log(res);
          this.radovi = res;
          this.prikaziRezultate = true;
        },
        error=>{
          console.log(error);
        }
      )
      
    }else if(this.tipPretrage == 3){

      let fraza = 0;
      if(this.frazaOznacena){
       fraza = 1; 
      }
      
      this.searchService.search(this.autori, fraza, 'autori').subscribe(
        res=>{
          console.log(res);
          this.radovi = res;
          this.prikaziRezultate = true;
        },
        error=>{
          console.log(error);
        }
      )

    }else if(this.tipPretrage == 4){

      let fraza = 0;
      if(this.frazaOznacena){
       fraza = 1; 
      }
      
      this.searchService.search(this.kljucniPojmovi, fraza, 'kljucniPojmovi').subscribe(
        res=>{
          console.log(res);
          this.radovi = res;
          this.prikaziRezultate = true;
        },
        error=>{
          console.log(error);
        }
      )
      
    }else if(this.tipPretrage == 5){

      let fraza = 0;
      if(this.frazaOznacena){
       fraza = 1; 
      }
      
      this.searchService.search(this.sadrzaj, fraza, "tekst").subscribe(
        res=>{
          console.log(res);
          this.radovi = res;
          this.prikaziRezultate = true;
        },
        error=>{
          console.log(error);
        }
      )
      
    }else if(this.tipPretrage == 6){

      let temp="";
      let i;

      for(i=0; i<this.naucneOblasti.length; i++){
        temp = temp.concat(this.naucneOblasti[i]);
        temp = temp.concat(', ');
      }
    
      if(temp==""){
        alert("Oaberite naucne oblasti");
        return;
      }
      
      temp = temp.substring(0,temp.length-1);
      console.log(temp);

      this.searchService.search(temp, 0, 'naucnaOblast').subscribe(
        res=>{
        this.radovi = res;
        this.prikaziRezultate = true;
        },
        error=>{
          console.log(error);
        }
      )
      
    }
  
  }

}
