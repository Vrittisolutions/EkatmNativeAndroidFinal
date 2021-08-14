uniform highp mat4 matProj;
uniform highp vec4 screenSize;
uniform highp mat4 matWorldView;
uniform highp mat4 matWorldViewProj;
uniform highp mat4 matDataTransform;
uniform highp vec4 PenThickness;
uniform highp vec4 NanPenThickness;
uniform highp vec4 PenColor;
attribute highp vec3 vPosition;
attribute highp vec3 vTexCoord0;
attribute highp vec4 vColor1;
attribute highp vec3 vTexCoord1;
attribute highp vec4 vColor2;
attribute highp vec3 vTexCoord2;
attribute highp vec4 vTexCoord4;
varying highp vec4 xlv_COLOR0;
varying highp vec3 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  highp mat4 tmpvar_2;
  tmpvar_2 = (matWorldView * matProj);
  highp vec3 tmpvar_3;
  highp vec3 tmpvar_4;
  highp float fTexOffset_5;
  highp float fThickness_6;
  highp float fNanAmount_7;
  highp vec3 tmpvar_8;
  highp vec3 tmpvar_9;
  highp vec3 tmpvar_10;
  highp float fLerpEdge_11;
  highp float tmpvar_12;
  tmpvar_12 = vTexCoord4.x;
  fLerpEdge_11 = vTexCoord4.z;
  fNanAmount_7 = 0.0;
  tmpvar_3 = vTexCoord0;
  tmpvar_4 = vTexCoord1;
  bool tmpvar_13;
  if ((((vTexCoord1.y < 0.0) || (0.0 < vTexCoord1.y)) || (vTexCoord1.y == 0.0))) {
    tmpvar_13 = bool(0);
  } else {
    tmpvar_13 = bool(1);
  };
  if (tmpvar_13) {
    tmpvar_4 = vTexCoord0;
    tmpvar_3 = vPosition;
    fNanAmount_7 = 1.0;
  };
  bool tmpvar_14;
  if ((((tmpvar_3.y < 0.0) || (0.0 < tmpvar_3.y)) || (tmpvar_3.y == 0.0))) {
    tmpvar_14 = bool(0);
  } else {
    tmpvar_14 = bool(1);
  };
  if (tmpvar_14) {
    tmpvar_3 = vPosition;
  };
  highp vec4 tmpvar_15;
  tmpvar_15 = mix (vColor2, vColor1, vec4(((1.0 - vTexCoord4.y) * PenThickness.z)));
  highp float tmpvar_16;
  tmpvar_16 = abs(vTexCoord4.w);
  if ((tmpvar_16 > 0.0)) {
    highp vec2 tmpvar_17;
    tmpvar_17.x = matDataTransform[0].x;
    tmpvar_17.y = matDataTransform[1].y;
    highp vec2 tmpvar_18;
    tmpvar_18.x = matDataTransform[0].x;
    tmpvar_18.y = matDataTransform[1].y;
    highp vec3 tmpvar_19;
    tmpvar_19.z = 0.0;
    tmpvar_19.xy = ((vTexCoord2.xy - tmpvar_4.xy) * tmpvar_18);
    highp vec3 tmpvar_20;
    tmpvar_20.z = 0.0;
    tmpvar_20.xy = ((tmpvar_4.xy - tmpvar_3.xy) * tmpvar_17);
    highp vec3 tmpvar_21;
    tmpvar_21 = ((tmpvar_19.yzx * tmpvar_20.zxy) - (tmpvar_19.zxy * tmpvar_20.yzx));
    if ((tmpvar_21.z > 0.0)) {
      fLerpEdge_11 = -(vTexCoord4.z);
    };
  };
  fThickness_6 = PenThickness.x;
  tmpvar_9.xy = mix (tmpvar_4.xy, vTexCoord2.xy, vTexCoord4.yy);
  tmpvar_10.xy = mix (vTexCoord2.xy, tmpvar_4.xy, vTexCoord4.yy);
  highp float tmpvar_22;
  tmpvar_22 = (PenThickness.x * fLerpEdge_11);
  fTexOffset_5 = abs((mix (tmpvar_4.z, vTexCoord2.z, 
    abs(vTexCoord4.y)
  ) / PenThickness.y));
  if (bool(fNanAmount_7)) {
    fThickness_6 = NanPenThickness.x;
    fTexOffset_5 = -1.0;
  };
  highp vec4 tmpvar_23;
  tmpvar_23.x = tmpvar_22;
  tmpvar_23.y = tmpvar_12;
  tmpvar_23.z = fThickness_6;
  tmpvar_23.w = fTexOffset_5;
  if ((vTexCoord4.w > 0.0)) {
    tmpvar_9.xy = tmpvar_4.xy;
    tmpvar_10.xy = tmpvar_3.xy;
  };
  highp vec4 tmpvar_24;
  tmpvar_24.xy = tmpvar_9.xy;
  tmpvar_24.zw = tmpvar_10.xy;
  highp vec4 tmpvar_25;
  highp vec4 tmpvar_26;
  tmpvar_26.x = matDataTransform[0].x;
  tmpvar_26.y = matDataTransform[1].y;
  tmpvar_26.z = matDataTransform[0].x;
  tmpvar_26.w = matDataTransform[1].y;
  highp vec4 tmpvar_27;
  tmpvar_27.x = matDataTransform[0].w;
  tmpvar_27.y = matDataTransform[1].w;
  tmpvar_27.z = matDataTransform[0].w;
  tmpvar_27.w = matDataTransform[1].w;
  tmpvar_25 = ((tmpvar_24 * tmpvar_26) + tmpvar_27);
  tmpvar_9.xy = tmpvar_25.xy;
  tmpvar_10.xy = tmpvar_25.zw;
  highp vec4 tmpvar_28;
  tmpvar_28.zw = vec2(0.0, 1.0);
  tmpvar_28.xy = tmpvar_8.xy;
  tmpvar_1 = (tmpvar_28 * tmpvar_2);
  highp vec4 tmpvar_29;
  tmpvar_29 = tmpvar_23;
  highp vec4 tmpvar_30;
  tmpvar_30 = vec4(0.0, 0.0, 0.0, 0.0);
  highp vec4 outPosition_31;
  highp vec4 tmpvar_32;
  tmpvar_32.zw = vec2(0.0, 1.0);
  tmpvar_32.xy = tmpvar_9.xy;
  highp vec4 tmpvar_33;
  tmpvar_33 = (tmpvar_32 * matWorldViewProj);
  outPosition_31 = (tmpvar_33 / abs(tmpvar_33.w));
  highp vec4 tmpvar_34;
  tmpvar_34.zw = vec2(0.0, 1.0);
  tmpvar_34.xy = tmpvar_10.xy;
  highp vec4 tmpvar_35;
  tmpvar_35 = (tmpvar_34 * matWorldViewProj);
  highp vec2 tmpvar_36;
  tmpvar_36 = normalize(((tmpvar_35 / 
    abs(tmpvar_35.w)
  ).xy - outPosition_31.xy));
  outPosition_31.x = (outPosition_31.x - ((tmpvar_22 * screenSize.z) * tmpvar_36.y));
  outPosition_31.y = (outPosition_31.y + ((tmpvar_22 * screenSize.w) * tmpvar_36.x));
  if ((PenThickness.x <= 1.0)) {
    tmpvar_29.y = 0.5;
  };
  tmpvar_30 = (tmpvar_15 * PenColor);
  highp vec3 tmpvar_37;
  tmpvar_37.x = tmpvar_29.y;
  tmpvar_37.y = tmpvar_29.w;
  tmpvar_37.z = tmpvar_29.z;
  tmpvar_1.xzw = outPosition_31.xzw;
  tmpvar_1.y = -(outPosition_31.y);
  gl_Position = tmpvar_1;
  xlv_COLOR0 = tmpvar_30;
  xlv_TEXCOORD0 = tmpvar_37;
  xlv_TEXCOORD1 = vec2(0.0, 0.0);
}

