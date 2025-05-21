<?php

define('host','localhost');
define('user','root');
define('password','');
define('database','android');

$conn = mysqli_connect(host,user,password,database);
if(!$conn){
    echo "koneksi gagal : " . mysqli_connect_error();
    exit();
}

?>