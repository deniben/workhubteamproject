import { Component, Input, OnInit } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { ImageCroppedEvent } from "ngx-image-cropper";

@Component({
  selector: "app-fileload",
  templateUrl: "./fileload.component.html",
  styleUrls: ["./fileload.component.css"]
})
export class FileloadComponent implements OnInit {
  imageChangedEvent: any = "";
  croppedImage;
  fileToUpload: File;
  image: HTMLImageElement;
  messageAboutSize;
  messageAboutType;
  photoName = [];
  invalidFile;
  imgURL: any;
  @Input()
  photoWidth: number;
  @Input()
  photoHeight: number;
  @Input() parent: FormGroup;

  constructor() {}

  imageCropped(event: ImageCroppedEvent) {
    this.croppedImage = event.base64;
    console.log(this.croppedImage);
    this.imgURL = event.base64;
  }

  preview(image: any): File {
    const loadedImage = image.target.files[0];
    if (loadedImage !== undefined) {
      this.imageChangedEvent = image;
      console.log(this.imageChangedEvent);
      this.fileToUpload = loadedImage;
      if (loadedImage.type.match(/image\/*/) == null) {
        this.messageAboutType = "Only images are supported";
        this.invalidFile = true;
        this.imgURL = null;
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(loadedImage);
        reader.onload = () => {
          this.imgURL = reader.result;
        };
        this.messageAboutType = "";
        this.invalidFile = false;
      }
    } else {
      this.imgURL = null;
      this.imageChangedEvent = null;
      this.croppedImage = null;
    }
    return image;
  }
  ngOnInit() {
    this.photoName = [];
  }

  postFile(): FormData {
    const file: FormData = new FormData();
    console.log(this.imgURL);
    this.fileToUpload = this.base64toFile(this.imgURL);
    const fileName = this.fileToUpload.name;
    console.log(this.fileToUpload);
    console.log(this.imgURL);
    file.append("file", this.fileToUpload, fileName);

    return file;
  }

  base64toFile(base64): File {
    const arr = base64.split(",");
    const mime = arr[0].match(/:(.*?);/)[1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }
    return new File(
      [u8arr],
      "file" +
        "." +
        this.imgURL.slice(
          this.imgURL.toString().indexOf("/") + 1,
          this.imgURL.toString().indexOf(";")
        ),
      { type: mime }
    );
  }
}
