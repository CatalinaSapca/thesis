import React from "react";
import {RegisterUser} from './utils/rest-calls'
import UserDTO from '../modelDTO/UserDTO'
import {BASE_URL} from '../pages/utils/constants';
import axios from "axios";
import { Outlet, Link } from "react-router-dom";
import { Navigate } from "react-router-dom";
import { ISLOGGED } from "../pages/utils/constants";
import { bcrypt } from "../pages/utils/constants" 
import validator from 'validator' ;

class Register extends React.Component{
  constructor(props) {
    super(props);
    this.state = {
      email: '',
      password: '',
      firstName: '',
      lastName: ''
    };

    this.handleChangeEmail = this.handleChangeEmail.bind(this);
    this.handleChangePassword = this.handleChangePassword.bind(this);
    this.handleChangeFirstName = this.handleChangeFirstName.bind(this);
    this.handleChangeLastName = this.handleChangeLastName.bind(this);
    this.handleRegister = this.handleRegister.bind(this);
    
    this.registrationFieldsISValid = this.registrationFieldsISValid.bind(this);

    this.hashingPassword = this.hashingPassword.bind(this);
  }

  handleChangeEmail(event) {
    this.setState({email : event.target.value})
  }

  handleChangePassword(event) {
    this.setState({password : event.target.value})
  }

  handleChangeFirstName(event) {
    this.setState({firstName : event.target.value})
  }

  handleChangeLastName(event) {
    this.setState({lastName : event.target.value})
  }

  handleRegister(event){
    event.preventDefault();

    if(this.registrationFieldsISValid()){

      RegisterUser({
        email: this.state.email,
        hashedPassword: this.hashingPassword(this.state.password),
        firstName: this.state.firstName,
        lastName: this.state.lastName
      })
      .then(response => {
        console.log(response);
        if(response === 200){ // register was succesful

          // set logged user to true
          localStorage.setItem('isLogged', true)
          localStorage.setItem('email', this.state.email)
          localStorage.setItem('password', this.state.password)

          //redirect to generation page
          window.location.replace('/generation');
        }
        else{
          window.location.replace('/register');
        }
    })
    // .then(data => 
      //window.location.replace('/generation'))
    .catch(error => {
        console.log(error);
        if(error.response.status === 401){ // account with this email already exists
          alert("email aldready used")
          this.setState({password: ""})
          document.getElementById("emailInputMessage").style.display = "block"
          document.getElementById("emailInputMessage").innerHTML = "email address already used by another account"
          document.getElementById("passwordInputMessage").style.display = "none"
          document.getElementById("passwordInputMessage").innerHTML = ""
          document.getElementById("firstnameInputMessage").style.display = "none"
          document.getElementById("firstnameInputMessage").innerHTML = ""
          document.getElementById("lastnameInputMessage").style.display = "none"
          document.getElementById("lastnameInputMessage").innerHTML = ""
          //window.location.replace('/login')
        }
        else if(error.response.status === 400){ // invalid fields
          alert("invalid fields")
          document.getElementById("emailInputMessage").style.display = "none"
          document.getElementById("passwordInputMessage").style.display = "none"
          document.getElementById("firstnameInputMessage").style.display = "none"
          document.getElementById("lastnameInputMessage").style.display = "none"
          document.getElementById("lastnameInputMessage").innerHTML = ""
          //window.location.replace('/login')
        }
        else{ //internal server error
          alert('internal server error')
          //window.location.replace('/login')
        }
      });
    }
  }

  // validation can be done step by step fi required
  // return in every if block
  // adding else if instead of ifssss
  registrationFieldsISValid(){
    var no = 0;

    document.getElementById("emailInputMessage").style.display = "none"
    document.getElementById("passwordInputMessage").style.display = "none"
    document.getElementById("firstnameInputMessage").style.display = "none"
    document.getElementById("lastnameInputMessage").style.display = "none"
   
    if(!validator.isEmail(this.state.email)){
      document.getElementById("emailInputMessage").style.display = "block"
      document.getElementById("emailInputMessage").innerHTML = "wrong email fromat"
      no = no + 1;
    }
    if(!validator.isStrongPassword(this.state.password)){
      document.getElementById("passwordInputMessage").style.display = "block"
      document.getElementById("passwordInputMessage").innerHTML = 'weak password'
      no = no + 1;
    }
    if(this.state.firstName.length === 0){
      document.getElementById("firstnameInputMessage").style.display = "block"
      document.getElementById("firstnameInputMessage").innerHTML = "invalid first name"
      no = no + 1;
    }
    if(this.state.lastName.length === 0){
      document.getElementById("lastnameInputMessage").style.display = "block"
      document.getElementById("lastnameInputMessage").innerHTML = "invalid last name"
      no = no + 1;
    }

    if(no > 0)
      return false;
    else
      return true;
  };

  hashingPassword(password){
    return bcrypt.hashSync(password, '$2a$10$CwTycUXWue0Thq9StjUM0u') // hash created previously created upon sign up
  }

  render(){
    return (
      <div  style={{  position: 'absolute', left: '50%', top: '50%',  transform: 'translate(-50%, -50%)'}}  >
        <meta charSet="utf-8" />
        <link rel="stylesheet" href="https://cdn.auth0.com/ulp/react-components/1.59.13/css/main.cdn.min.css" />
        <link rel="stylesheet" href="./styling.css" />
        <style id="custom-styles-container" dangerouslySetInnerHTML={{__html: "\n    " }} />
        <header>
        <div class="center-image">
            <a>
              <img alt="tattOOtopy logo" height={32} src="https://i.ibb.co/jkL3c3n/logo.png" style={{display: 'block', height: 'auto', border: 0}} title="Instagram" width={130} />
            </a>
          </div>  
        </header>
        <main className="_widget c43f81caf">
          <section className="c69c8adb5">
            <div>
              <div>
                <header>
                  <h1 className="c8ccd131c">Create Your Account</h1>
                  <br></br>
                </header>

                <div className="c62acd940">
                  <form className="_form-login-password c2d54d3ec" onSubmit={this.handleRegister}>
                    <div>
                      <div>
                        <div>
                        <div class="_input-wrapper input-wrapper">
                          <div class="text cdaa5633a text" data-alternate-action-text="" data-action-text="">
                            <label class="c2b602944 no-js cac31896e c46a18555" for="email"> Email address </label>
                            <input class="input cab61d8c8 cef66eec3" inputMode="email" name="email" id="email" type="text" required autoComplete="username" autoCapitalize="none" spellCheck="false" value={this.state.email} onChange={this.handleChangeEmail} autoFocus/>
                            <label id="emailInputMessage" className="" style={{display:'none', color: 'red', height: '8px', fontSize: '12px', float: 'left'}}> invalid email address </label>    
                          </div>  
                        </div>

                        <div class="input-wrapper _input-wrapper">
                          <div class="text cdaa5633a text" data-alternate-action-text="" data-action-text="">
                            <label class="c2b602944 no-js cac31896e c46a18555" for="firstName"> First name </label>
                            <input class="input cab61d8c8 cef66eec3" inputMode="text" name="firstName" id="firstName" type="text" required autoComplete="first name" autoCapitalize="none" spellCheck="false" value={this.state.firstName} onChange={this.handleChangeFirstName}/>
                            <label id="firstnameInputMessage" className="" style={{display:'none', color: 'red', height: '8px', fontSize: '12px', float: 'left'}}> invalid first name </label>    
                          </div>  
                        </div>

                        <div class="input-wrapper _input-wrapper">
                          <div class="text cdaa5633a text" data-alternate-action-text="" data-action-text="">
                            <label class="c2b602944 no-js cac31896e c46a18555" for="lastName"> Last name </label>
                            <input class="input cab61d8c8 cef66eec3" inputMode="text" name="lastName" id="lastName" type="text" required autoComplete="last name" autoCapitalize="none" spellCheck="false" value={this.state.lastName} onChange={this.handleChangeLastName}/>
                            <label id="lastnameInputMessage" className="" style={{display:'none', color: 'red', height: '8px', fontSize: '12px', float: 'left'}}> invalid last name </label>    
                          </div>  
                        </div>

                          <div className="input-wrapper _input-wrapper">
                            <div className="cdaa5633a c2ff41511 password cbe83985a" data-action-text data-alternate-action-text>
                              <label className="c2b602944 register-doesnt-require-js " htmlFor="password"> Password </label>
                              <input className="input cbcf0d22b cab61d8c8" name="password" id="password" type="password" required autoComplete="new-password" autoCapitalize="none" spellCheck="false" value={this.state.password} onChange={this.handleChangePassword}/>
                              <label id="passwordInputMessage" className="" style={{display:'none', color: 'red', height: '8px', fontSize: '12px', float: 'left'}}> invalid password </label>    
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="st10 no-arrow ce28e72d1 c4d141802 ca6696a54 c79d246a4 ce92fcc5c c20ce63af _hide" aria-live="assertive" aria-atomic="true">
                        <div className="c5ff91825 cd53c67b6">
                          <ul className="c7e1a6b25">
                            <li className="c19fbf129 c0afec543">
                              <div className="cc9adf877 c19fbf129">
                                <span className="cd2571556">Your password must contain:</span>
                                <ul className="c516dd640">
                                  <li className="c1c5367c9 st11" data-error-code="password-policy-length-at-least">
                                    <span className="cd2571556">At least 8 characters</span>
                                  </li>  
                                </ul>
                              </div>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <div className="c31e91fde">
                      <button type="submit" name="action" value="default" className="c55316539 st8 ca2869ec9 c448bcaa9 c763d828f">register</button>    
                    </div>
                  </form>
                  <div className="ulp-alternate-action  _alternate-action ">
                    <p>Already have an account? 
                      <a href="/login"> Log in</a>
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>
        </main>
      </div>
    );
  }
};

export default Register;
