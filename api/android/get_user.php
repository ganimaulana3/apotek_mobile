<?php
include 'koneksi.php';

$id_user = $_POST['id_user'];

$response = [];

$query = mysqli_query($conn, "SELECT * FROM tbl_pelanggan WHERE id = '$id_user'");

if (mysqli_num_rows($query) > 0) {
    $data = mysqli_fetch_assoc($query);
    $response['status'] = 1;
    $response['user'] = $data;
} else {
    $response['status'] = 0;
    $response['message'] = "User tidak ditemukan";
}

echo json_encode($response);
