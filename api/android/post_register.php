<?php
include "koneksi.php";
header('Content-Type: application/json');

$email = mysqli_real_escape_string($conn, $_POST['email']);
$nama = mysqli_real_escape_string($conn, $_POST['nama']);
$password = mysqli_real_escape_string($conn, $_POST['password']);

$getstatus = 0;
$getresult = 0;
$message = "";

// Cek apakah email sudah terdaftar
$sql = "SELECT * FROM tbl_pelanggan WHERE email='$email'";
$hasil = mysqli_query($conn, $sql);

if (mysqli_num_rows($hasil) > 0) { // Jika email sudah ada
    $getstatus = 0;
    $message = "User sudah ada";
} else {
    // Insert data pengguna baru
    $sql = "INSERT INTO tbl_pelanggan(nama, email, password) VALUES ('$nama', '$email', MD5('$password'))";
    $hasil = mysqli_query($conn, $sql);

    if ($hasil) {
        $getstatus = 1;
        $getresult = 1;
        $message = "Simpan berhasil";
    } else {
        $getstatus = 1;
        $getresult = 0;
        $message = "Simpan gagal: " . mysqli_error($conn);
    }
}

// Pastikan JSON selalu dikembalikan
echo json_encode(array('status' => $getstatus, 'result' => $getresult, 'message' => $message));

?>
