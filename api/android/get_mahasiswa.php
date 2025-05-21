<?php

include "koneksi.php";
header('content-type: application/json');

$sql = "select * from mahasiswa";
$hasil = mysqli_query($conn,$sql);

$result = array();
$no = 0;

while($data = mysqli_fetch_object($hasil)){
    array_push($result,array('nim'=>$data->nim,'nama'=>$data->nama,'telp'=>$data->telp,'email'=>$data->email));
}

echo json_encode(array('result'=>$result));

?>