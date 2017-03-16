package com.xingyuyou.xingyuyou.download;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 下午8:11
 */
@Table(name = "download", onCreated = "CREATE UNIQUE INDEX index_name ON download(label,fileSavePath)")
public class DownloadInfo {

    public DownloadInfo() {
    }

    @Column(name = "id", isId = true)
    private long id;

    @Column(name = "state")
    private DownloadState state = DownloadState.STOPPED;

    @Column(name = "url")
    private String url;

    @Column(name = "gameSize")
    private String gameSize;

    @Column(name = "gameIntro")
    private String gameIntro;

    @Column(name = "gamePicUrl")
    private String gamePicUrl;

    @Column(name = "label")
    private String label;

    @Column(name = "fileSavePath")
    private String fileSavePath;

    @Column(name = "progress")
    private int progress;

    @Column(name = "fileLength")
    private long fileLength;

    @Column(name = "autoResume")
    private boolean autoResume;

    @Column(name = "autoRename")
    private boolean autoRename;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DownloadState getState() {
        return state;
    }

    public void setState(DownloadState state) {
        this.state = state;
    }
    public String getGameSize() {
        return gameSize;
    }

    public void setGameSize(String gameSize) {
        this.gameSize = gameSize;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getGamePicUrl() {
        return gamePicUrl;
    }

    public void setGamePicUrl(String gamePicUrl) {
        this.gamePicUrl = gamePicUrl;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }
    public String getGameIntro() {
        return gameIntro;
    }

    public void setGameIntro(String gameIntro) {
        this.gameIntro = gameIntro;
    }
    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename() {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename) {
        this.autoRename = autoRename;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "id=" + id +
                ", state=" + state +
                ", url='" + url + '\'' +
                ", gameSize='" + gameSize + '\'' +
                ", gameIntro='" + gameIntro + '\'' +
                ", gamePicUrl='" + gamePicUrl + '\'' +
                ", label='" + label + '\'' +
                ", fileSavePath='" + fileSavePath + '\'' +
                ", progress=" + progress +
                ", fileLength=" + fileLength +
                ", autoResume=" + autoResume +
                ", autoRename=" + autoRename +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadInfo)) return false;

        DownloadInfo that = (DownloadInfo) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
