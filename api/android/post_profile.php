<?php
include "koneksi.php";
header('Content-Type: application/json');

// Cek apakah semua parameter dikirim melalui POST
if (!isset($_POST['email'], $_POST['nama'], $_POST['alamat'], $_POST['kota'], $_POST['provinsi'], $_POST['telp'], $_POST['kodepos'])) {
    echo json_encode(array('result' => 0, 'message' => 'Semua field harus diisi.'));
    exit();
}

$email = $_POST['email'];
$nama = $_POST['nama'];
$alamat = $_POST['alamat'];
$kota = $_POST['kota'];
$provinsi = $_POST['provinsi'];
$telp = $_POST['telp'];
$kodepos = $_POST['kodepos'];

$getresult = 0;
$message = "";

// ✅ Gunakan Prepared Statement untuk keamanan
$sql = "UPDATE tbl_pelanggan SET nama=?, alamat=?, kota=?, provinsi=?, telp=?, kodepos=? WHERE email=?";
$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "sssssss", $nama, $alamat, $kota, $provinsi, $telp, $kodepos, $email);
$hasil = mysqli_stmt_execute($stmt);

if ($hasil) {
    $getresult = 1;
    $message = "Update Berhasil";
} else {
    $getresult = 0;
    $message = "Update Gagal: " . mysqli_error($conn);
}

// Tutup statement dan koneksi
mysqli_stmt_close($stmt);
mysqli_close($conn);

// Kirim response JSON
echo json_encode(array('result' => $getresult, 'message' => $message));
?>
