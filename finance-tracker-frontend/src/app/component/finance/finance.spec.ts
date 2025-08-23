import { TestBed } from '@angular/core/testing';
import { Finance } from './finance';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Finance],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(Finance);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(Finance);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Hello, finance-tracker-frontend');
  });
});
