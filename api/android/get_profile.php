<?php
include "koneksi.php";
header('Content-Type: application/json');

// Pastikan parameter `email` tersedia
if (!isset($_GET['email'])) {
    echo json_encode(array('result' => 0, 'message' => 'Email harus disertakan.'));
    exit();
}

$email = $_GET['email'];
$datauser = array();
$getstatus = 0;

// Gunakan Prepared Statement untuk menghindari SQL Injection
$sql = "SELECT * FROM tbl_pelanggan WHERE email = ?";
$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "s", $email);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);
$data = mysqli_fetch_object($result);

if (!$data) {
    echo json_encode(array('result' => 0, 'message' => 'Data tidak ditemukan.'));
} else {
    $getstatus = 1;
    $datauser = array(
        'email' => $data->email,
        'nama' => $data->nama,
        'alamat' => $data->alamat,
        'kota' => $data->kota,
        'provinsi' => $data->provinsi,
        'kodepos' => $data->kodepos,
        'telp' => $data->telp
    );

    echo json_encode(array('result' => $getstatus, 'data' => $datauser));
}

// Tutup statement
mysqli_stmt_close($stmt);
mysqli_close($conn);
?>
