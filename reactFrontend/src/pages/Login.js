 import React from "react";
 import {LoginUser} from './utils/rest-calls'
 import UserDTO from '../modelDTO/UserDTO'
 import {BASE_URL} from '../pages/utils/constants';
 import axios from "axios";
 import { Outlet, Link } from "react-router-dom";
 import { Navigate } from "react-router-dom";
 import { bcrypt } from "../pages/utils/constants" ;

class Login extends React.Component{
  constructor(props) {
    super(props);
    this.state = {
      email: '',
      password: '',
      passwordShown: false
    };

    this.handleChangeEmail = this.handleChangeEmail.bind(this);
    this.handleChangePassword = this.handleChangePassword.bind(this);
    this.handleLogin = this.handleLogin.bind(this);

    this.togglePassword = this.togglePassword.bind(this);

    this.hashingPassword = this.hashingPassword.bind(this);
  }

  handleChangeEmail(event) {
    this.setState({email : event.target.value})
  }

  handleChangePassword(event) {
    this.setState({password : event.target.value})
  }

  handleLogin(event){
    event.preventDefault();
    // alert(this.hashingPassword(this.state.password));

    LoginUser({
      email: this.state.email,
      hashedPassword: this.hashingPassword(this.state.password)
    })
    .then(response => {
      console.log(response);
      if(response === 200){ // login was succesful

        // set logged user to true
        localStorage.setItem('isLogged', true)
        localStorage.setItem('email', this.state.email)
        localStorage.setItem('password', this.state.password)

        // redirect to generation page
        window.location.replace('/generation');
        
      }
      else{
        window.location.replace('/login');
      }
  })
  // .then(data => 
    //window.location.replace('/generation'))
  .catch(error => {
      console.log(error);
      if(error.response.status === 401){ // wrong password, correct email
        alert("wrong password")
        this.setState({password: ""})
        document.getElementById("passwordInputMessage").style.display = "block"
        document.getElementById("passwordInputMessage").innerHTML = "invalid password"
        document.getElementById("emailInputMessage").style.display = "none"
        document.getElementById("emailInputMessage").innerHTML = ""
        //window.location.replace('/login')
      }
      if(error.response.status === 400){ // email is null
        alert("email cannot be null")
        document.getElementById("emailInputMessage").style.display = "block"
        document.getElementById("emailInputMessage").innerHTML = "email cannot be null"
        document.getElementById("passwordInputMessage").style.display = "none"
        document.getElementById("passwordInputMessage").innerHTML = ""
        //window.location.replace('/login')
      }
      else if(error.response.status === 404){ // user with given email address not found
        alert("user not found")
        this.setState({email: ""})
        this.setState({password: ""})
        document.getElementById("emailInputMessage").style.display = "block"
        document.getElementById("emailInputMessage").innerHTML = "invalid email adress"
        document.getElementById("passwordInputMessage").style.display = "none"
        document.getElementById("passwordInputMessage").innerHTML = ""
        //window.location.replace('/login')
      }
      else{ //internal server error
        alert('internal server error')
        //window.location.replace('/login')
      }
    });
  }

  togglePassword(event){
      this.setState({passwordShown : !this.state.passwordShown})
  };

  hashingPassword(password){
    return bcrypt.hashSync(password, '$2a$10$CwTycUXWue0Thq9StjUM0u') // hash created previously created upon sign up
  }

  render(){
    return (
      <div>
      <div style={{  position: 'absolute', left: '50%', top: '50%',  transform: 'translate(-50%, -50%)'}} >
        <meta charSet="utf-8" />
        <link rel="stylesheet" href="https://cdn.auth0.com/ulp/react-components/1.59.13/css/main.cdn.min.css" />
        <link rel="stylesheet" href="styling.css" />
        <header>
          <div href="/" >
          <div className="center-image" >
            <a>
              <img alt="tattOOtopy logo" height={32} src="https://i.ibb.co/jkL3c3n/logo.png" style={{height: 'auto', border: 0, display: 'block'}} title="tattOOtopy logo" width={130} />
            </a>
          </div>
          </div>
        </header><main>
          <section className="c69c8adb5">
            <div style={{display: 'flex',  justifyContent:'center', alignItems:'center'}}>
              <div>
                <header>
                  <h1 className="c8ccd131c" >Welcome</h1>
                  <br></br>
                </header>
                <div className="c62acd940 caa02071a">
                  <form className="c2d54d3ec _form-login-password" onSubmit={this.handleLogin} >
                    <div>
                      <div>
                        <div className="emailadress_div input-wrapper">
                          <div className="st4 c2ff41511 text c083604f6" data-action-text data-alternate-action-text>
                            <label className="c2b602944 this-isnt-js cac31896e c7e047387" htmlFor="username" > Email address </label>
                            <input className="input" inputMode="email" name="username" id="username" type="text" required autoComplete="username" autoCapitalize="none" spellCheck="false" value={this.state.email} onChange={this.handleChangeEmail}/>
                            <label id="emailInputMessage" className="" style={{display:'none', color: 'red', height: '8px', fontSize: '12px', float: 'left'}}> invalid email address </label>    
                          </div>
                        </div>
                        <div className="_input-wrapper password_div input-wrapper">
                          <div className="st4 c2ff41511 password cbe83985a" data-action-text data-alternate-action-text>
                            <label className="c2b602944 this-isnt-js" htmlFor="password" placeholder="Enter Password"> Password </label>
                            <input className="input" type={this.state.passwordShown ? "text" : "password"} id="password" name="password" required autoComplete="current-password" autoCapitalize="none" spellCheck="false" value={this.state.password} onChange={this.handleChangePassword} />
                            <label id="passwordInputMessage" className="" style={{display:'none', color: 'red', height: '8px', fontSize: '12px', float: 'left'}}> invalid password </label>
                            <button type="button" style={{ color: 'grey', cursor: 'pointer', float: 'right', opacity: '0.555' }} className=" ulp-button-icon ca2869ec9 _button-icon" onClick={this.togglePassword}>Show Password  </button>
                          </div>
                        </div>
                      </div>
                    </div>
                    <br></br>
                    <p>
                      <a href="/reset-password">Forgot password?</a>
                    </p>
                    <div>
                      <button type="submit" name="action" value="default" className="st1 _button-login-password c55316539">login</button>
                    </div>
                  </form>
                  <div className="__s16nu9 ulp-alternate-action">
                    <p>Don't have an account?
                      <a href="/register"> Register</a>
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>
        </main>
      </div>
      </div>
    );
  }
};





//!function(){var e,t,v,h,r,n={exports:function(r,n){var a={};function i(e,t,r,n){return e.addEventListener(t,r,n)}function o(e){return"string"==typeof e}function c(e,t){return e.getAttribute(t)}function s(e,t,r){return e.setAttribute(t,r)}return{addClass:function(e,t){if(e.classList)return e.classList.add(t);var r=e.className.split(" ");-1===r.indexOf(t)&&(r.push(t),e.className=r.join(" "))},toggleClass:function(e,t){if(e.classList)return e.classList.toggle(t);var r=e.className.split(" "),n=r.indexOf(t);-1!==n?r.splice(n,1):r.push(t),e.className=r.join(" ")},addClickListener:function(e,t){return i(e,"click",t)},addEventListener:i,getAttribute:c,getElementById:function(e){return n.getElementById(e)},getParent:function(e){return e.parentNode},isString:o,loadScript:function(e){var t=n.createElement("script");t.src=e,t.async=!0,n.body.appendChild(t)},poll:function(e){var i=e.interval||2e3,t=e.url||r.location.href,o=e.condition||function(){return!0},c=e.onSuccess||function(){},s=e.onError||function(){};return setTimeout(function n(){var a=new XMLHttpRequest;return a.open("GET",t),a.setRequestHeader("Accept","application/json"),a.onload=function(){if(200===a.status){var e="application/json"===a.getResponseHeader("Content-Type").split(";")[0]?JSON.parse(a.responseText):a.responseText;return o(e)?c():setTimeout(n,i)}if(429!==a.status)return s({status:a.status,responseText:a.responseText});var t=1e3*Number.parseInt(a.getResponseHeader("X-RateLimit-Reset")),r=t-(new Date).getTime();return setTimeout(n,i<r?r:i)},a.send()},i)},querySelector:function(e,t){return o(e)?n.querySelector(e):e.querySelector(t)},querySelectorAll:function(e,t){var r=o(e)?n.querySelectorAll(e):e.querySelectorAll(t);return Array.prototype.slice.call(r)},removeClass:function(e,t){if(e.classList)return e.classList.remove(t);var r=e.className.split(" "),n=r.indexOf(t);-1!==n&&(r.splice(n,1),e.className=r.join(" "))},setAttribute:s,removeAttribute:function(e,t){return e.removeAttribute(t)},swapAttributes:function(e,t,r){var n=c(e,t),a=c(e,r);s(e,r,n),s(e,t,a)},setGlobalFlag:function(e,t){a[e]=!!t},getGlobalFlag:function(e){return!!a[e]},preventFormSubmit:function(e){e.stopPropagation(),e.preventDefault()},matchMedia:function(e){return"function"!=typeof r.matchMedia&&r.matchMedia(e).matches},dispatchEvent:function(e,t){e.dispatchEvent(t)},setTimeout:setTimeout}}}.exports(window,document),a={exports:function(e,t){for(var i="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_",l=new Uint8Array(256),o=0;o<i.length;o++)l[i.charCodeAt(o)]=o;function c(e){var t,r=new Uint8Array(e),n=r.length,a="";for(t=0;t<n;t+=3)a+=i[r[t]>>2],a+=i[(3&r[t])<<4|r[t+1]>>4],a+=i[(15&r[t+1])<<2|r[t+2]>>6],a+=i[63&r[t+2]];return n%3==2?a=a.substring(0,a.length-1):n%3==1&&(a=a.substring(0,a.length-2)),a}function r(){return navigator&&navigator.credentials&&"undefined"!=typeof PublicKeyCredential}return{base64URLEncode:c,base64URLDecode:function(e){var t,r,n,a,i,o=.75*e.length,c=e.length,s=0,u=new Uint8Array(o);for(t=0;t<c;t+=4)r=l[e.charCodeAt(t)],n=l[e.charCodeAt(t+1)],a=l[e.charCodeAt(t+2)],i=l[e.charCodeAt(t+3)],u[s++]=r<<2|n>>4,u[s++]=(15&n)<<4|a>>2,u[s++]=(3&a)<<6|63&i;return u.buffer},publicKeyCredentialToJSON:function e(t){if(t instanceof Array){var r=[];for(o=0;o<t.length;o+=1)r.push(e(t[o]));return r}if(t instanceof ArrayBuffer)return c(t);if(t instanceof Object){var n={};for(var a in t)n[a]=e(t[a]);return n}return t},str2ab:function(e){for(var t=new ArrayBuffer(e.length),r=new Uint8Array(t),n=0,a=e.length;n<a;n++)r[n]=e.charCodeAt(n);return t},isWebAuthnAvailable:r,isWebauthnPlatformAuthenticatorAvailableAsync:function(){return r()?PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable():Promise.resolve(!1)}}}}.exports(window,document);((e={}).exports=function(n,e,o,c,s,u,l){e("div.cdaa5633a.password").forEach(function(e){var a,i,t=n(e,"input"),r=n(e,'[data-action="toggle"]');o(e,(a=t,i=r,function(e){if(e.target.classList.contains("ulp-button-icon")){if(a.type="password"===a.type?"text":"password",i){var t=i.querySelector(".show-password-tooltip"),r=i.querySelector(".hide-password-tooltip");t&&u(t,"c20ce63af"),r&&u(r,"c20ce63af")}var n=l(a);"text"===a.type?c(n,"c88145e97"):s(n,"c88145e97")}}))})},e.exports)(n.querySelector,n.querySelectorAll,n.addClickListener,n.addClass,n.removeClass,n.toggleClass,n.getParent),{exports:function(e,n,a,t){var r=e(".c7b266003"),i=e("#alert-trigger"),o=e(".cf102ec62"),c=e(".ce6692f55"),s=!1;i&&c&&r&&t(r,function(e){var t=e.target===i,r=c.contains(e.target);return t&&!s?(n(o,"c88145e97"),void(s=!0)):t&&s||s&&!r?(a(o,"c88145e97"),void(s=!1)):void 0})}}.exports(n.querySelector,n.addClass,n.removeClass,n.addClickListener),(v="recaptcha_v2",h="recaptcha_enterprise",(t={}).exports=function(e,a,i,o,c,n){var s,u=a("div[data-recaptcha-sitekey]"),t=a("div[data-recaptcha-sitekey] input"),l=a("#ulp-recaptcha");function f(){return u.getAttribute("data-recaptcha-provider")}function d(e){return t.value=e}function p(e,t){if(e&&e.getBoundingClientRect){if(!n("(max-width: 480px)"))return l.style.transform="",void(l.style.height="");(void 0===t||isNaN(t))&&(t=1.4);var r=72*t;l.style.transform="scale("+t+")",l.style.height=r+"px",l.style.width="10px",u.clientWidth+8<e.getBoundingClientRect().width&&p(e,t-.01)}}u&&(s="recaptchaCallback_"+Math.floor(1000001*Math.random()),window[s]=function(){var e,t,r,n;delete window[s],e=function(e){switch(e){case v:return window.grecaptcha;case h:return window.grecaptcha.enterprise}}(f()),t=e.render(l,{sitekey:u.getAttribute("data-recaptcha-sitekey"),"expired-callback":function(){d(""),i(u,"c2635a5ac"),e.reset(t)},callback:function(e){d(e),o(u,"c2635a5ac")}}),r=function(e){p(e),c(window,"resize",function(){p(e)})},n=setInterval(function(){var e=a("#ulp-recaptcha iframe");if(e)return clearInterval(n),r(e)},200)},e(function(e,t,r){switch(e){case v:return"https://www.recaptcha.net/recaptcha/api.js?hl="+t+"&onload="+r;case h:return"https://www.recaptcha.net/recaptcha/enterprise.js?render=explicit&hl="+t+"&onload="+r}}(f(),u.getAttribute("data-recaptcha-lang"),s)))},t.exports)(n.loadScript,n.querySelector,n.addClass,n.removeClass,n.addEventListener,n.matchMedia),((r={}).exports=function(n,e,a,i,o,c,s,u,r,l){function f(e){var t=e.target,r=c(t);t.value||l(t,"data-autofilled")?i(r,"c1b43dee6"):o(r,"c1b43dee6")}function d(e){var t=e.target;"onAutoFillStart"===e.animationName&&(r(t,"data-autofilled",!0),u(e.target,new Event("change",{bubbles:!0})),a(t,"keyup",p,{once:!0}))}function p(e){var t=e.target;r(t,"data-autofilled","")}if(n("body._simple-labels"))return e(".c2b602944.no-js").forEach(function(e){o(e,"no-js")}),void e(".c2b602944.js-required").forEach(function(e){i(e,"c20ce63af")});e(".cdaa5633a:not(.c4482f2ec):not(disabled)").forEach(function(e){i(e,"c8c2b35f6");var t,r=n(e,".input");r.value&&i(e,"c1b43dee6"),a(e,"change",f),a(r,"blur",f),a(r,"animationstart",d),t=r,s(function(){t.value&&u(t,new Event("change",{bubbles:!0}))},100)})},r.exports)(n.querySelector,n.querySelectorAll,n.addEventListener,n.addClass,n.removeClass,n.getParent,n.setTimeout,n.dispatchEvent,n.setAttribute,n.getAttribute),{exports:function(e,t,r,n,a,i){function o(e){var t=r("submitted");n("submitted",!0),t?a(e):"apple"===i(e.target,"data-provider")&&setTimeout(function(){n("submitted",!1)},2e3)}var c=e("form");c&&c.forEach(function(e){t(e,"submit",o)})}}.exports(n.querySelectorAll,n.addEventListener,n.getGlobalFlag,n.setGlobalFlag,n.preventFormSubmit,n.getAttribute),{exports:function(t,e){var r=t("form._form-detect-browser-capabilities"),n=t("main.login-id");if(r||n){var a=e.isWebAuthnAvailable();t("#webauthn-available").value=a?"true":"false",t("#js-available").value="true",navigator.brave?navigator.brave.isBrave().then(function(e){t("#is-brave").value=e,i()}):i()}function i(){a?e.isWebauthnPlatformAuthenticatorAvailableAsync().then(function(e){t("#webauthn-platform-available").value=e?"true":"false",r&&r.submit()}).catch(function(e){r&&r.submit()}):(t("#webauthn-platform-available").value="false",r&&r.submit())}}}.exports(n.querySelector,a)}();

// function updateHeader(text) {
//         const $h1 = document.querySelector('main header > h1');
//         if ($h1) {
//             $h1.innerText = text;
//         }
//     }
    
    
    
// updateHeader('Welcome');

export default Login;