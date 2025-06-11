<?php
include "koneksi.php";
header('content-type: application/json');

$order = $_POST['order'];
$order_detail = $_POST['order_detail'];

$hasilorder = json_decode($order);
$hasilorder_detail = json_decode($order_detail);

$sql = "SELECT IFNULL(MAX(trans_id)+1,1) AS trans_id FROM tbl_order";
$hasil = mysqli_query($conn,$sql);
$data = mysqli_fetch_array($hasil);

$id = $data['trans_id'];
$email = $hasilorder->email;
$tgl_order = date('Y-m-d', time());
$subtotal = $hasilorder->subtotal;
$ongkir = $hasilorder->ongkir;
$total_bayar = $hasilorder->total_bayar;
$alamat_kirim = $hasilorder->alamat_kirim;
$telp_kirim = $hasilorder->telp_kirim;

?>