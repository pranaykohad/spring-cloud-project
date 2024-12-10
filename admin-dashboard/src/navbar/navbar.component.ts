import { Component, Input } from '@angular/core';
import { SystemUserResponse } from '../models/SystemUserResponse';


@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  @Input()
  systemUserResponse!: SystemUserResponse;
  urbanShowsLogo :string="assets/img/Urban Shows Logo.png";

}
