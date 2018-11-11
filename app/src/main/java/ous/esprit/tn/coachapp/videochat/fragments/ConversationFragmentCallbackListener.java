package ous.esprit.tn.coachapp.videochat.fragments;

import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback;

import org.webrtc.CameraVideoCapturer;

import ous.esprit.tn.coachapp.videochat.activities.CallActivity;

/**
 * Created by tereha on 23.05.16.
 */
public interface ConversationFragmentCallbackListener {

    void addTCClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks);
    void removeRTCClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks);

    void addRTCSessionEventsCallback(QBRTCSessionEventsCallback eventsCallback);
    void removeRTCSessionEventsCallback(QBRTCSessionEventsCallback eventsCallback);

    void addCurrentCallStateCallback (CallActivity.CurrentCallStateCallback currentCallStateCallback);
    void removeCurrentCallStateCallback (CallActivity.CurrentCallStateCallback currentCallStateCallback);

    void addOnChangeDynamicToggle (CallActivity.OnChangeDynamicToggle onChangeDynamicCallback);
    void removeOnChangeDynamicToggle (CallActivity.OnChangeDynamicToggle onChangeDynamicCallback);

    void onSetAudioEnabled(boolean isAudioEnabled);

    void onSetVideoEnabled(boolean isNeedEnableCam);

    void onSwitchAudio();

    void onHangUpCurrentSession();

    void onStartScreenSharing();

    void onSwitchCamera(CameraVideoCapturer.CameraSwitchHandler cameraSwitchHandler);
}
