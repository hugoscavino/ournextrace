import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'ijudy-about-us',
  templateUrl: './about-us.component.html',
  styleUrls: ['./about-us.component.css']
})
export class AboutUsComponent implements OnInit {

  public _showSideMenu: boolean;

  constructor() { }

  ngOnInit() {
  }
  public showSideMenu(value: boolean) {
    this._showSideMenu = value;
  }

}
