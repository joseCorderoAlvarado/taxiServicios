<?php

    $url = "https://fcm.googleapis.com/fcm/send";
    $token = "eRRaNXEcukQ:APA91bE6d6gcxmLkJG-HPhEUULCk7HJoCQquaSw5-g1fLhsFY4jE5b_2whHE-4-kIKLqROCxg-F_MtQQlqjd2w3xRfbV-KF4RMzpslGifk9--BwHJ8QxPNA9KPFSHNXu9zCFHVf2sN_G";
    $serverKey = "AAAAXUTjKWQ:APA91bGG-gEZS6I0cpp6_bXVPtGhMTULeQworcUNugZyUb1-lJksLcC9fRlBGHcB1d6RXWEbvWQhd78GXg1oQacRChlpdxg9tqxZr5u1PAj3637l1aNfrqiZ5UsY9stIC1ygxtfcwcXN";
    $title = "Servicio Nuevo";
    $body = "Hipo ha pedido un servicio";
    $notification = array('title' =>$title , 'body' => $body, 'sound' => 'default', 'badge' => '1');
    $arrayToSend = array('to' => $token, 'notification' => $notification,'priority'=>'high');
    $json = json_encode($arrayToSend);
    $headers = array();
    $headers[] = 'Content-Type: application/json';
    $headers[] = 'Authorization: key='. $serverKey;
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST,"POST");
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_HTTPHEADER,$headers);
    //Send the request
    $response = curl_exec($ch);
    //Close request
    if ($response === FALSE) {
    die('FCM Send Error: ' . curl_error($ch));
    }
    curl_close($ch);
?>

