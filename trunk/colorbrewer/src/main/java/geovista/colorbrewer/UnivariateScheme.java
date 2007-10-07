package geovista.colorbrewer;

import java.awt.Color;

import edu.psu.geovista.common.color.Palette;



/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class UnivariateScheme
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Biliang Zhou
 * @version 1.0
 */

public class UnivariateScheme implements Palette{

  String name;
  int hclass;
  int ranking;
  Color[] colorinRGB;
  SchemeSuitability suitabilitydataset = new SchemeSuitability();
  int[][] currentSchemeSuitability = new int[14][6];


  public static final int maxUnivariateClasses = 15;

  public UnivariateScheme() {
    colorinRGB = new Color[maxUnivariateClasses];

    if(this.name == "YlGn"){
      currentSchemeSuitability = SchemeSuitability.YlGn;
    }
    if(this.name == "YlGnBu"){
      currentSchemeSuitability = SchemeSuitability.YlGnBu;
    }
    if(this.name == "GnBu"){
      currentSchemeSuitability = SchemeSuitability.GnBu;
    }
    if(this.name == "BuGn"){
      currentSchemeSuitability = SchemeSuitability.BuGn;
    }
    if(this.name == "PuBuGn"){
      currentSchemeSuitability = SchemeSuitability.PuBuGn;
    }
    if(this.name == "PuBu"){
      currentSchemeSuitability = SchemeSuitability.PuBu;
    }
    if(this.name == "BuPu"){
      currentSchemeSuitability = SchemeSuitability.BuPu;
    }
    if(this.name == "RdPu"){
      currentSchemeSuitability = SchemeSuitability.RdPu;
    }
    if(this.name == "PuRd"){
      currentSchemeSuitability = SchemeSuitability.PuRd;
    }
    if(this.name == "OrRd"){
      currentSchemeSuitability = SchemeSuitability.OrRd;
    }
    if(this.name == "YlOrRd"){
      currentSchemeSuitability = SchemeSuitability.YlOrRd;
    }
    if(this.name == "YlOrBr"){
      currentSchemeSuitability = SchemeSuitability.YlOrBr;
    }
    if(this.name == "Purples"){
      currentSchemeSuitability = SchemeSuitability.Purples;
    }
    if(this.name == "Blues"){
      currentSchemeSuitability = SchemeSuitability.Blues;
    }
    if(this.name == "Greens"){
      currentSchemeSuitability = SchemeSuitability.Greens;
    }
    if(this.name == "Oranges"){
      currentSchemeSuitability = SchemeSuitability.Oranges;
    }
    if(this.name == "Reds"){
      currentSchemeSuitability = SchemeSuitability.Reds;
    }
    if(this.name == "Grays"){
      currentSchemeSuitability = SchemeSuitability.Grays;
    }


    if(this.name == "PuOr"){
      currentSchemeSuitability = SchemeSuitability.PuOr;
    }
    if(this.name == "BrBG"){
      currentSchemeSuitability = SchemeSuitability.BrBG;
    }
    if(this.name == "PRGn"){
      currentSchemeSuitability = SchemeSuitability.PRGn;
    }
    if(this.name == "PiYG"){
      currentSchemeSuitability = SchemeSuitability.PiYG;
    }
    if(this.name == "RdBu"){
      currentSchemeSuitability = SchemeSuitability.RdBu;
    }
    if(this.name == "RdGy"){
      currentSchemeSuitability = SchemeSuitability.RdGy;
    }
    if(this.name == "RdYlBu"){
      currentSchemeSuitability = SchemeSuitability.RdYlBu;
    }
    if(this.name == "Spectral"){
      currentSchemeSuitability = SchemeSuitability.Spectral;
    }
    if(this.name == "RdYlGn"){
      currentSchemeSuitability = SchemeSuitability.RdYlGn;
    }


    if(this.name == "Set1"){
      currentSchemeSuitability = SchemeSuitability.Set1;
    }
    if(this.name == "Pastel1"){
      currentSchemeSuitability = SchemeSuitability.Pastel1;
    }
    if(this.name == "Set2"){
      currentSchemeSuitability = SchemeSuitability.Set2;
    }
    if(this.name == "Pastel2"){
      currentSchemeSuitability = SchemeSuitability.Pastel2;
    }
    if(this.name == "Dark2"){
      currentSchemeSuitability = SchemeSuitability.Dark2;
    }
    if(this.name == "Set3"){
      currentSchemeSuitability = SchemeSuitability.Set3;
    }
    if(this.name == "Paired"){
      currentSchemeSuitability = SchemeSuitability.Paired;
    }
    if(this.name == "Accents"){
      currentSchemeSuitability = SchemeSuitability.Accents;
    }

  }

  public void overWrite(UnivariateScheme scheme1){

    scheme1.name = this.name;
    scheme1.hclass = this.hclass;
    scheme1.ranking = this.ranking;

    for(int i = 0; i < hclass; i ++){
      scheme1.colorinRGB[i] = this.colorinRGB[i];
    }

  }

  //implementing the UnivariatePalette interface
  public Color[] getColors(int length){

    Color univariatescheme[] = new Color[length];
    for(int i = 0; i < length; i++){
      univariatescheme[i] = this.colorinRGB[i];
    }
    return univariatescheme;
  }

  public String getUnivariateName(){
    return this.name;
  }

  public int getRecommendedMaxLength(){
    int recommendedMaxLength = 9;
    if(this.name == "YlGn" || this.name == "YlGnBu" || this.name == "GnBu" || this.name == "BuGn" || this.name == "PuBuGn" || this.name == "PuBu"|| this.name == "BuPu"|| this.name == "RdPu"|| this.name == "PuRd"|| this.name == "OrRd"|| this.name == "YlOrRd"|| this.name == "YlOrBr"|| this.name == "Purples"|| this.name == "Blues"|| this.name == "Greens"|| this.name == "Oranges"|| this.name == "Reds"|| this.name == "Grays"){
      recommendedMaxLength = 9;
    }
    if(this.name == "PuOr" || this.name == "BrBG"|| this.name == "PRGn"|| this.name == "PiYG"|| this.name == "RdBu"|| this.name == "RdGy"|| this.name == "RdYlBu"|| this.name == "Spectral"|| this.name == "RdYlGn"){
      recommendedMaxLength = 11;
    }
    if(this.name == "Set1" || this.name == "Pastel1"|| this.name == "Set2"|| this.name == "Pastel2"|| this.name == "Dark2"|| this.name == "Set3"|| this.name == "Paired"|| this.name == "Accents"){
      recommendedMaxLength = 8;
    }
    return recommendedMaxLength;
  }

  public int getUnivariateType(){
    int univariateType = SEQUENTIAL;
    if(this.name == "YlGn" || this.name == "YlGnBu" || this.name == "GnBu" || this.name == "BuGn" || this.name == "PuBuGn" || this.name == "PuBu"|| this.name == "BuPu"|| this.name == "RdPu"|| this.name == "PuRd"|| this.name == "OrRd"|| this.name == "YlOrRd"|| this.name == "YlOrBr"|| this.name == "Purples"|| this.name == "Blues"|| this.name == "Greens"|| this.name == "Oranges"|| this.name == "Reds"|| this.name == "Grays"){
      univariateType = SEQUENTIAL;
    }
    if(this.name == "PuOr" || this.name == "BrBG"|| this.name == "PRGn"|| this.name == "PiYG"|| this.name == "RdBu"|| this.name == "RdGy"|| this.name == "RdYlBu"|| this.name == "Spectral"|| this.name == "RdYlGn"){
      univariateType = DIVERGING;
    }
    if(this.name == "Set1" || this.name == "Pastel1"|| this.name == "Set2"|| this.name == "Pastel2"|| this.name == "Dark2"|| this.name == "Set3"|| this.name == "Paired"|| this.name == "Accents"){
      univariateType = QUALITATIVE;
    }
    return univariateType;
  }

  public boolean isSequential(){
    if(this.name == "YlGn" || this.name == "YlGnBu" || this.name == "GnBu" || this.name == "BuGn" || this.name == "PuBuGn" || this.name == "PuBu"|| this.name == "BuPu"|| this.name == "RdPu"|| this.name == "PuRd"|| this.name == "OrRd"|| this.name == "YlOrRd"|| this.name == "YlOrBr"|| this.name == "Purples"|| this.name == "Blues"|| this.name == "Greens"|| this.name == "Oranges"|| this.name == "Reds"|| this.name == "Grays"){
      return true;
    }
    else{
      return false;
    }
  }

  public boolean isDivergent(){
    if(this.name == "PuOr" || this.name == "BrBG"|| this.name == "PRGn"|| this.name == "PiYG"|| this.name == "RdBu"|| this.name == "RdGy"|| this.name == "RdYlBu"|| this.name == "Spectral"|| this.name == "RdYlGn"){
      return true;
    }
    else{
      return false;
    }
  }

  public boolean isQualitative(){
    if(this.name == "Set1" || this.name == "Pastel1"|| this.name == "Set2"|| this.name == "Pastel2"|| this.name == "Dark2"|| this.name == "Set3"|| this.name == "Paired"|| this.name == "Accents"){
      return true;
    }
    else{
      return false;
    }
  }

  public int isCRTSafe(int length){
    if((length > this.getRecommendedMaxLength())||(length < 2)){
      return NA;
    }
    else{
      if(this.currentSchemeSuitability[4][length - 2] == GOOD){
        return GOOD;
      }
      if(this.currentSchemeSuitability[4][length - 2] == DOUBTFUL){
        return DOUBTFUL;
      }
      if(this.currentSchemeSuitability[4][length - 2] == BAD){
        return BAD;
      }
      else{
        return NA;
      }
    }
  }

  public int isColorblindSafe(int length){
    if((length > this.getRecommendedMaxLength())||(length < 2)){
      return NA;
    }
    else{
      if(this.currentSchemeSuitability[0][length - 2] == GOOD){
        return GOOD;
      }
      if(this.currentSchemeSuitability[0][length - 2] == DOUBTFUL){
        return DOUBTFUL;
      }
      if(this.currentSchemeSuitability[0][length - 2] == BAD){
        return BAD;
      }
      else{
        return NA;
      }
    }
  }

  public int isPhotocopySafe(int length){
    if((length > this.getRecommendedMaxLength())||(length < 2)){
      return NA;
    }
    else{
      if(this.currentSchemeSuitability[1][length - 2] == GOOD){
        return GOOD;
      }
      if(this.currentSchemeSuitability[1][length - 2] == DOUBTFUL){
        return DOUBTFUL;
      }
      if(this.currentSchemeSuitability[1][length - 2] == BAD){
        return BAD;
      }
      else{
        return NA;
      }
    }
  }

  public int isColorPrintingSafe(int length){
    if((length > this.getRecommendedMaxLength())||(length <= 2)){
      return NA;
    }
    else{
      if(this.currentSchemeSuitability[5][length - 2] == GOOD){
        return GOOD;
      }
      if(this.currentSchemeSuitability[5][length - 2] == DOUBTFUL){
        return DOUBTFUL;
      }
      if(this.currentSchemeSuitability[5][length - 2] == BAD){
        return BAD;
      }
      else{
        return NA;
      }
    }
  }

  public int isLCDprojectorSafe(int length){
    if((length > this.getRecommendedMaxLength())||(length < 2)){
      return NA;
    }
    else{
      if(this.currentSchemeSuitability[2][length - 2] == GOOD){
        return GOOD;
      }
      if(this.currentSchemeSuitability[2][length - 2] == DOUBTFUL){
        return DOUBTFUL;
      }
      if(this.currentSchemeSuitability[2][length - 2] == BAD){
        return BAD;
      }
      else{
        return NA;
      }
    }
  }

  public int isLaptopSafe(int length){
    if((length > this.getRecommendedMaxLength())||(length < 2)){
      return NA;
    }
    else{
      if(this.currentSchemeSuitability[3][length - 2] == GOOD){
        return GOOD;
      }
      if(this.currentSchemeSuitability[3][length - 2] == DOUBTFUL){
        return DOUBTFUL;
      }
      if(this.currentSchemeSuitability[3][length - 2] == BAD){
        return BAD;
      }
      else{
        return NA;
      }
    }
  }


}