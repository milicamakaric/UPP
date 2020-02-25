import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ZuulPath } from 'src/app/ZuulPath';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private http: HttpClient, private zuul: ZuulPath) { }

  getNaucneOblasti(){
    return this.http.get(this.zuul.path + 'pretraga/naucneOblasti') as Observable<any>;
  }

  // searchByNazivCasopisa(nazivCasopisa, fraza){
  //   return this.http.get(this.zuul.path + 'pretraga/byNazivCasopisa/' + fraza + '/' + nazivCasopisa) as Observable<any>;
  // }

  // searchByNaslovRada(naslovRada, fraza){
  //   return this.http.get(this.zuul.path + 'pretraga/byNaslovRada/' + fraza + '/' + naslovRada) as Observable<any>;
  // }

  // searchByAutori(autori, fraza){
  //   return this.http.get(this.zuul.path + 'pretraga/byAutori/' + fraza + '/' + autori) as Observable<any>;
  // }

  // searchByKljucniPojmovi(kljucniPojmovi, fraza){
  //   return this.http.get(this.zuul.path + 'pretraga/byKljucniPojmovi/' + fraza + '/' + kljucniPojmovi) as Observable<any>;
  // }

  // searchBySadrzaj(sadrzaj, fraza){
  //   return this.http.get(this.zuul.path + 'pretraga/bySadrzaj/' + fraza + '/' + sadrzaj) as Observable<any>;
  // }

  // searchByNaucneOblasti(naucneOblasti){
  //   return this.http.get(this.zuul.path + 'pretraga/byNaucneOblasti/' + naucneOblasti) as Observable<any>;
  // }

  search(parametar, fraza, tip){
    console.log(parametar + ' ' + fraza + ' ' + tip);
    return this.http.get(this.zuul.path + 'pretraga/customPretraga/' + fraza + '/' + parametar + '/' + tip) as Observable<any>;
  }

  advancedSearch(o){
    return this.http.post(this.zuul.path + 'pretraga/advancedPretraga', o) as Observable<any>;
  }

  searchByGeoSpacing(procesId){
    return this.http.get(this.zuul.path + 'pretraga/geoSpacing/' + procesId) as Observable<any>;
  }

  searchByMoreLikeThis(procesId){
    return this.http.get(this.zuul.path + 'pretraga/moreLikeThis/' + procesId) as Observable<any>;
  }

  searchByNaucneOblasti(procesId){
    return this.http.get(this.zuul.path + 'pretraga/naucneOblasti/' + procesId) as Observable<any>;
  }

  searchAll(procesId){
    return this.http.get(this.zuul.path + 'pretraga/all/' + procesId) as Observable<any>;
  }

}
