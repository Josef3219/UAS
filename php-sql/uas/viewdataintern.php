<?php
    require_once __DIR__ . '/koneksi.php';
    $db = new DB_CONNECT();

    header("Content-Type:application/json");

    $parameter = array(
        'username' => $_POST['username'],
        'peran' => $_POST['peran']
    );

    $response = array(
        'error' => FALSE,
        'error_text' => "",
        'data' => array(),
    );

    if($response['error'] == FALSE){
        if ($parameter['peran'] == "admin") {
            // admin dapat mengambil data semua intern
            $result = mysqli_query($db->connect(), "SELECT * FROM intern");
        } else {
            // user hanya dapat melihat datanya sendiri         
            $result = mysqli_query($db->connect(), "SELECT * FROM intern INNER JOIN account ON intern.email = account.email WHERE username = '".$parameter['username']."'");
        }
        
        if (mysqli_num_rows($result) > 0) {
            $hasil = array();
            while ($col = mysqli_fetch_assoc($result)) {
                if ($col['jenkel'] == 'L') {
                    $jenkel = "Laki-laki";
                } else { // jika kolom jenkel bernilai L, maka data yang direturn adalah laki-laki, dan sebaliknya
                    $jenkel = "Perempuan";
                }
                $intern = array(
                    'id' => $col['id'],
                    'nama' => $col['nama'],
                    'umur' => $col['umur'],
                    'jenkel' => $jenkel,
                    'email' => $col['email'],
                    'alamat' => $col['alamat'],
                    'telpon' => $col['telpon'],
                    'gambar' => $col['gambar'],
                    'performa' => $col['performa'],
                    'project' => $col['project'],
                );
                $hasil[$col['id']] = $intern;
            }
            $response['data'] = $hasil;
        }
    }
    // menamplikan json dari variabel response
    echo json_encode($response, JSON_PRETTY_PRINT);

   