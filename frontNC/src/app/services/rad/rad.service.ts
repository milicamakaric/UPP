import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ZuulPath } from 'src/app/ZuulPath';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RadService {

  constructor(private http: HttpClient, private zuul: ZuulPath) { }

  startProcess(){
    return this.http.get(this.zuul.path + 'rad/get') as Observable<any>;
  }

  postIzborCasopisa(izborCasopisa, taskId) {
    return this.http.post(this.zuul.path + "rad/postIzborCasopisa/".concat(taskId), izborCasopisa) as Observable<any>;
  }

  placanje(potprocesId){
    return this.http.get(this.zuul.path + 'rad/placanje/'.concat(potprocesId)) as Observable<any>;
  }

  postPlacanje(placanje, taskId) {
    return this.http.post(this.zuul.path + "rad/postPlacanje/".concat(taskId), placanje) as Observable<any>;
  }

  radInfo(procesId){
    return this.http.get(this.zuul.path + 'rad/radInfo/'.concat(procesId)) as Observable<any>;
  }

  postRadInfo(radInfo, taskId) {
    return this.http.post(this.zuul.path + "rad/postRadInfo/".concat(taskId), radInfo) as Observable<any>;
  }

  koautor(procesId){
    return this.http.get(this.zuul.path + 'rad/koautor/'.concat(procesId)) as Observable<any>;
  }

  postKoautorData(koautorData, taskId) {
    return this.http.post(this.zuul.path + "rad/postKoautorData/".concat(taskId), koautorData) as Observable<any>;
  }

  getGlavniUrednikTasks(){
    return this.http.get(this.zuul.path + 'rad/getGlavniUrednikTasks') as Observable<any>;
  }

  postGlavniUrednikData(glavniUrednikData, taskId) {
    return this.http.post(this.zuul.path + "rad/postGlavniUrednikData/".concat(taskId), glavniUrednikData) as Observable<any>;
  }

  getRadPdf(procesId){
    return this.http.get(this.zuul.path + 'rad/getRadPdf/'.concat(procesId)) as Observable<any>;
  }

  postPregledPdfData(pregledPdfData, taskId) {
    return this.http.post(this.zuul.path + "rad/postPregledPdfData/".concat(taskId), pregledPdfData) as Observable<any>;
  }

  getKorigovanjePdfAutorTask(){
    return this.http.get(this.zuul.path + 'rad/getKorigovanjePdfAutorTask') as Observable<any>;
  }

  postKorigovanjePdfData(korigovanjePdfData, taskId) {
    return this.http.post(this.zuul.path + "rad/postKorigovanjePdfData/".concat(taskId), korigovanjePdfData) as Observable<any>;
  }

  getVremeRecenziranjaTask(){
    return this.http.get(this.zuul.path + 'rad/getVremeRecenziranjaTask') as Observable<any>;
  }

  postVremeRecenziranjaData(vremeRecenziranja, taskId) {
    return this.http.post(this.zuul.path + "rad/postVremeRecenziranjaData/".concat(taskId), vremeRecenziranja) as Observable<any>;
  }

  getIzborRecenzenataTask(){
    return this.http.get(this.zuul.path + 'rad/getIzborRecenzenataTask') as Observable<any>;
  }

  postIzborRecenenataData(izborRecenzenata, taskId) {
    return this.http.post(this.zuul.path + "rad/postIzborRecenenataData/".concat(taskId), izborRecenzenata) as Observable<any>;
  }

  getRecenziranjeTask(){
    return this.http.get(this.zuul.path + 'rad/getRecenziranjeTask') as Observable<any>;
  }

  postRecenziranjeData(recenziranjeData, taskId) {
    return this.http.post(this.zuul.path + "rad/postRecenziranjeData/".concat(taskId), recenziranjeData) as Observable<any>;
  }

  getNoviRecenzentiTasks(){
    return this.http.get(this.zuul.path + 'rad/getNoviRecenzentiTasks') as Observable<any>;
  }

  postNoviRecenentData(noviRecenzent, taskId) {
    return this.http.post(this.zuul.path + "rad/postNoviRecenentData/".concat(taskId), noviRecenzent) as Observable<any>;
  }

  getAnalizaRecenzijaTask(){
    return this.http.get(this.zuul.path + 'rad/getAnalizaRecenzijaTask') as Observable<any>;
  }

  postAnalizaRecenzijaData(analizaRecenzija, taskId) {
    return this.http.post(this.zuul.path + "rad/postAnalizaRecenzijaData/".concat(taskId), analizaRecenzija) as Observable<any>;
  }

  getIspravkaAutorTask(){
    return this.http.get(this.zuul.path + 'rad/getIspravkaAutorTask') as Observable<any>;
  }

  postIspravkaAutorData(ispravkaAutor, taskId) {
    return this.http.post(this.zuul.path + "rad/postIspravkaAutorData/".concat(taskId), ispravkaAutor) as Observable<any>;
  }

  getPregledIspravljenogRadaTask(){
    return this.http.get(this.zuul.path + 'rad/getPregledIspravljenogRadaTask') as Observable<any>;
  }

  postPregledIspravljenogRadaData(pregledRada, taskId) {
    return this.http.post(this.zuul.path + "rad/postPregledIspravljenogRadaData/".concat(taskId), pregledRada) as Observable<any>;
  }

  postFile(procesId, data) {
    return this.http.post(this.zuul.path + "rad/uploadFile/".concat(procesId), data, {responseType: 'text'}) as Observable<any>;
  }

  downloadFile(procesId) {
    const httpOptions = {
      'responseType'  : 'arraybuffer' as 'json'
    };
    return this.http.get(this.zuul.path + "rad/downloadFile/".concat(procesId), httpOptions) as Observable<any>;
  }

  downloadRad(radId) {
    const httpOptions = {
      'responseType'  : 'arraybuffer' as 'json'
    };
    return this.http.get(this.zuul.path + "rad/downloadRad/".concat(radId), httpOptions) as Observable<any>;
  }


}
