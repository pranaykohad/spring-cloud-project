import { Component, ViewEncapsulation } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [SharedModule, LoginComponent, RegistrationComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  backGroundImage: string = 'assets/img/smoke-wave.jpg';

  constructor() {}
}
