<?php
include "koneksi.php";
header('Content-Type: application/json');

$email = mysqli_real_escape_string($conn, $_POST['email']);
$password = mysqli_real_escape_string($conn, $_POST['password']);

$response = array();

$sql = "SELECT * FROM tbl_pelanggan WHERE email='$email' AND password=MD5('$password')";
$hasil = mysqli_query($conn, $sql);

if (mysqli_num_rows($hasil) > 0) {
    $user = mysqli_fetch_assoc($hasil);
    
    $response['status'] = 1;
    $response['message'] = "Login berhasil";
    $response['user'] = array(
        "id" => $user['id'],
        "nama" => $user['nama'],
        "email" => $user['email']
    );
} else {
    $response['status'] = 0;
    $response['message'] = "Email atau password salah";
}

echo json_encode($response);
?>
