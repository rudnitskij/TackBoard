function action1(mark) {
    document.getElementById(mark).style.backgroundColor = 'lemonchiffon';
}
function action2(mark) {
    document.getElementById(mark).style.backgroundColor = 'ButtonFace';
}

function prohibited(){
    alert("Вы не можете редактировать\nили удалять объявления\nдругих пользователей");
}

function checkboxProcessing(URL,author) {
    var c = document.getElementById('checkbox');
    if (c.checked) {
        location.href = URL + "/"+author;
    } else {
        location.href = URL + ".html" ;
    }
}
