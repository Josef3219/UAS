<?php
    require_once __DIR__ . '/koneksi.php';
    $db = new DB_CONNECT();

    header("Content-Type:application/json");

    $parameter = array(
        'username' => $_POST['username'],
        'fullname' => $_POST['fullname'],
        'peran' => $_POST['peran']
    );

    $response = array(
        'error' => FALSE,
        'error_text' => "",
        'data' => array(),
    );

    if($response['error'] == FALSE){
        if ($parameter['peran'] == "admin") {
            // admin dapat mengambil data semua project
            $result = mysqli_query($db->connect(), "SELECT * FROM project");
        } else {
            // user hanya dapat melihat project yang dia kerjakan
            $result = mysqli_query($db->connect(), "SELECT * FROM project INNER JOIN intern ON project.pic = intern.nama WHERE pic = '".$parameter['fullname']."'");
        }
        
        if (mysqli_num_rows($result) > 0) {
            $hasil = array();
            while ($col = mysqli_fetch_assoc($result)) {
                $project = array(
                    'id' => $col['id'],
                    'namaproject' => $col['namaproject'],
                    'pic' => $col['pic'],
                    'startdate' => $col['startdate'],
                    'enddate' => $col['enddate']
                );
                $hasil[$col['id']] = $project;
            }
            $response['error'] = FALSE;
            $response['error_text'] = "Berhasil";
            $response['data'] = $hasil;
        } else {
            $response['error'] = TRUE;
            $response['error_text'] = "No Project is found";
        }
    }

    echo json_encode($response, JSON_PRETTY_PRINT);

   