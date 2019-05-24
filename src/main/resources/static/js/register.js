function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

var currentUser;
$.ajax({
    type:"Get",
    url:"http://localhost:8080/user/getCurrentUserDetail",
    headers:{
        "Authorization" : "Chess " + getUrlVars()["token"],
        "alg" : "HS512",
        "typ" : "JWT"
    },

}).done(function(response){
    if(response.data){
        currentUser = response.data;
        document.getElementsByName("fullName")[0].value = currentUser.fullName;
    }
});

var cetificate = function(link){
    var self = this;
    self.cetificateLink = link;
    return this;
};
document.getElementById("register").addEventListener("click",function(){
    currentUser.phone = document.getElementsByName("phone")[0].value;
    currentUser.district = document.getElementsByName("district")[0].value;
    currentUser.city = document.getElementsByName("city")[0].value;
    currentUser.achievement = document.getElementsByName("achievement")[0].value;
    currentUser.role = document.getElementsByName("role")[0].value;

    var cetificates = [];
    cetificates.push(new cetificate('test'));
    cetificates.push(new cetificate('testAPI'));
    currentUser.cetificates = cetificates;
    $.ajax({
        method:"PUT",
        url:"http://localhost:8080/user/register",
        headers:{
            "Authorization" : "Chess " + getUrlVars()["token"],
            "alg" : "HS512",
            "typ" : "JWT"
        },
        contentType: "application/json",
        dataType:"json",
        data: JSON.stringify(currentUser)
    }).done(function(response){
        if(response.data){
            document.location.href = response.message;//redirect url
        }else{
            alert(response.message);//message error
        }
    });
});

// Token authencation include: Chess + Token after login success