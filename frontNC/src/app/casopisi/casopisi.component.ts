import { Component, OnInit } from '@angular/core';
import { KpService } from '../services/kp/kp.service';
import { RadService } from '../services/rad/rad.service';

@Component({
  selector: 'app-casopisi',
  templateUrl: './casopisi.component.html',
  styleUrls: ['./casopisi.component.css']
})
export class CasopisiComponent implements OnInit {

  
  private casopisi = [];
  private nacini_placanja = [];
  private izabrani_rad_id;
  private dobijeni_nacini_placanja = false;

  constructor(private kpService: KpService, private radService: RadService) {

    kpService.getCasopisi().subscribe(
      res => {
        console.log(res);
        this.casopisi = res;

      },
      err => {
        console.log("Error occured");
      }
    );
   }


  ngOnInit() {
    
  }

  kupiRad(casopisId, radId){
    console.log('radId: ' + radId);

    this.radService.kupiRad(radId).subscribe(
      res => {
        alert('Uspesno ste kupili rad');
        window.location.href='casopisi';
      }, err => {
        alert("Error");
      }
    );

    // window.location.href = '/odabir_placanja'.concat('/' + casopisId + '/' + radId);
  }

  subscribe(casopisId){
    window.location.href = '/plan/create/'.concat(casopisId);
  }
  preuzmiRad(radId){
    // TODO
    console.log('preuzmi rad: ' + radId);

    this.radService.downloadRad(radId).subscribe(
      res => {
        var blob = new Blob([res], {type: 'application/pdf'});
        var url= window.URL.createObjectURL(blob);
        window.open(url, "_blank");
      }, err => {
        alert("Error while download file");
      }
    );
  }

  nazad(){
    window.location.href='';
  }

}
