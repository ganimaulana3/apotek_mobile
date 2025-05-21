<?php
include "koneksi.php";
header('content-type: application/json');

$nim = $_POST['nim'];
$nama = $_POST['nama'];
$telp = $_POST['telp'];
$email = $_POST['email'];

$sql = "insert into mahasiswa (nim,nama,telp,email)
        values ('$nim','$nama','$telp','$email')";
$hasil = mysqli_query($conn,$sql);

$kode = 0;
$pesan = "";
if($hasil){
    $kode = 1;
    $pesan = "Simpan data berhasil";
}else{
    $kode = 0;
    $pesan = "Simpan data gagal, kesalahan : ".mysqli_error($conn);
}
$status['kode'] = $kode;
$status['pesan'] = $pesan;

echo json_encode($status);
?>