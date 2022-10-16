import {BASE_URL} from './constants';
import axios from "axios";

function status(response) {
    console.log('response status '+response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
}

function json(response) {
    return response.json()
}

export async function LoginUser(user){
    //alert('LoginUser called');
    const response = await axios.post(BASE_URL+'/login', JSON.stringify(user),
        {
        headers: {
           'Content-Type': 'application/json',
        }}
    );
    return response.status;
}

export async function RegisterUser(user){
    //alert('RegisterUser called');
    const response = await axios.post(BASE_URL+'/register', JSON.stringify(user),
        {
        headers: {
           'Content-Type': 'application/json',
        }}
    );
    return response.status;
}

export async function GetAllTattoosOfUser(user){
    // alert('GetAllTattoosOfUser called');
    const response = await axios.post(BASE_URL+'/all-tattoos-of-user', JSON.stringify({email:"sapca.catalina@gmail.com", hashedPassword: "aaa"}),
        {
        headers: {
           'Content-Type': 'application/json',
        }}
    );
    //alert(response.data.data)
    // response.data.forEach(function(tattooItem) {  
    //     alert(tattooItem.id)
    // });

    return response;
}

export async function DeleteTattoo(id){
    // alert('DeleteTattoo called');
    const response = await axios.delete(BASE_URL+'/delete-tattoo/'+ id,
        {
        headers: {
           'Content-Type': 'application/json',
        }}
    );
    //alert(response.data.data)
    // response.data.forEach(function(tattooItem) {  
    //     alert(tattooItem.id)
    // });

    return response;
}




export async function UpdateTattoo(tattoo){
    // alert('UpdateTattoo called');
    const response = await axios.post(BASE_URL+'/update-tattoo/', JSON.stringify(tattoo),
        {
        headers: {
           'Content-Type': 'application/json',
        }}
    );
    //alert(response.data.data)
    // response.data.forEach(function(tattooItem) {  
    //     alert(tattooItem.id)
    // });

    return response;
}

export async function GenerateTattooWithoutImage(formData){
    // alert('GenerateTattooWithoutImage called');
    const response = await axios({
        method: "post",
        url: BASE_URL+'/generate-tattoo-without-image/',
        data: formData,
        headers: { "Content-Type": "multipart/form-data" },
      });
    //   alert('Generate tattoo with image done')
    return response;
}

export async function GenerateTattooWithImage(formData){
    // alert('GenerateTattooWithImage called');
    const response = await axios({
        method: "post",
        url: BASE_URL+'/generate-tattoo-with-image/',
        data: formData,
        headers: { "Content-Type": "multipart/form-data" },
      });
    //   alert('Generate tattoo with image done')
    return response;
}