import { Component } from '@angular/core';
import { SharedModule } from '../shared/shared.module';

@Component({
  selector: 'app-add-security-person',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './add-security-person.component.html',
  styleUrl: './add-security-person.component.scss'
})
export class AddSecurityPersonComponent {

}
