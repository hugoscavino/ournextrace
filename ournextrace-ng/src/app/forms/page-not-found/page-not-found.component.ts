import { Component, OnInit } from '@angular/core';
import { environment} from '../../../environments/environment';
import { MetadataService} from '../../service/metadata';

@Component({
  selector: 'ijudy-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.css']
})
export class PageNotFoundComponent implements OnInit {

  public home_page_404: string;
  public env: string;

  constructor(private metadataService: MetadataService) { }

  ngOnInit() {
    this.home_page_404 = environment.home_page_404;
    this.metadataService.getEnvironment().subscribe(
      data => {
        this.env = data.env;
      }
    );
  }

  public isNotProd() {
    return this.env !== 'prod';
  }

}
