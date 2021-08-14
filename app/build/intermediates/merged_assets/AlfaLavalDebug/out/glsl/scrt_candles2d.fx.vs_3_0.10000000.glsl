uniform highp vec4 screenSize;
uniform highp mat4 matWorldViewProj;
uniform highp mat4 matDataTransform;
uniform highp vec4 PenThicknessA;
uniform highp vec4 PenThicknessB;
uniform highp vec4 CandleWidth;
attribute highp float vPosition;
attribute highp vec4 vTexCoord0;
attribute highp vec4 vColor;
attribute highp vec4 vColor1;
attribute highp vec2 vTexCoord1;
attribute highp vec4 vTexCoord2;
attribute highp vec4 vTexCoord3;
attribute highp vec4 vTexCoord4;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  highp vec4 tmpvar_3;
  highp vec2 tmpvar_4;
  highp vec4 tmpvar_5;
  highp vec4 tmpvar_6;
  highp float tmpvar_7;
  tmpvar_7 = ((vPosition * matDataTransform[0].x) + matDataTransform[0].w);
  highp vec4 tmpvar_8;
  tmpvar_8 = ((vTexCoord0 * matDataTransform[1].yyyy) + matDataTransform[1].wwww);
  tmpvar_2 = tmpvar_8;
  highp float tmpvar_9;
  tmpvar_9 = vTexCoord1.x;
  highp float tmpvar_10;
  if ((tmpvar_8.x > tmpvar_8.w)) {
    tmpvar_10 = 0.0;
  } else {
    tmpvar_10 = 1.0;
  };
  if (((tmpvar_10 > 0.0) && (tmpvar_8.y < tmpvar_8.z))) {
    tmpvar_2.y = tmpvar_8.z;
    tmpvar_2.z = tmpvar_8.y;
  };
  if ((vTexCoord1.x == -1.0)) {
    highp vec4 tmpvar_11;
    tmpvar_11.zw = vec2(0.0, 1.0);
    tmpvar_11.x = (tmpvar_7 + mix ((
      -(CandleWidth.x)
     * 0.5), (CandleWidth.x * 0.5), vTexCoord1.y));
    tmpvar_11.y = dot (tmpvar_2, vTexCoord2);
    tmpvar_3 = tmpvar_11;
    tmpvar_4 = vTexCoord1;
    tmpvar_5 = vTexCoord2;
    tmpvar_6 = vTexCoord4;
    if ((tmpvar_10 == 0.0)) {
      tmpvar_6.y = (1.0 - vTexCoord4.y);
    };
  } else {
    highp vec4 tmpvar_12;
    tmpvar_12 = mix (PenThicknessA, PenThicknessB, vec4(tmpvar_10));
    highp float fEdgeDirection_13;
    fEdgeDirection_13 = ((tmpvar_10 * 2.0) - 1.0);
    highp vec4 tmpvar_14;
    highp vec4 tmpvar_15;
    tmpvar_15.zw = vec2(0.0, 0.0);
    highp float tmpvar_16;
    tmpvar_16 = (-(CandleWidth.x) * 0.5);
    highp float tmpvar_17;
    tmpvar_17 = (CandleWidth.x * 0.5);
    highp vec2 tmpvar_18;
    tmpvar_18.x = (tmpvar_7 + mix (tmpvar_16, tmpvar_17, vTexCoord1.y));
    tmpvar_18.y = (dot (tmpvar_2, vTexCoord2) - ((vTexCoord4.w * tmpvar_12.x) * fEdgeDirection_13));
    highp vec2 tmpvar_19;
    tmpvar_19.x = (tmpvar_7 + mix (tmpvar_16, tmpvar_17, vTexCoord4.x));
    tmpvar_19.y = (dot (tmpvar_2, vTexCoord3) - ((vTexCoord4.w * tmpvar_12.x) * fEdgeDirection_13));
    highp vec2 x_20;
    x_20 = (tmpvar_18 - tmpvar_19);
    highp float tmpvar_21;
    tmpvar_21 = ((sqrt(
      dot (x_20, x_20)
    ) / tmpvar_12.y) * abs(vTexCoord4.y));
    tmpvar_14.xy = mix (tmpvar_18, tmpvar_19, vTexCoord4.yy);
    tmpvar_14.zw = mix (tmpvar_19, tmpvar_18, vTexCoord4.yy);
    highp vec4 tmpvar_22;
    tmpvar_22.y = 0.0;
    tmpvar_22.x = (tmpvar_12.x * vTexCoord4.z);
    tmpvar_22.z = tmpvar_12.x;
    tmpvar_22.w = tmpvar_21;
    highp vec2 tmpvar_23;
    tmpvar_23.x = 0.0;
    tmpvar_23.y = tmpvar_21;
    tmpvar_15.xy = tmpvar_23;
    tmpvar_3 = tmpvar_14;
    tmpvar_4 = tmpvar_22.xy;
    tmpvar_5 = vec4(0.0, 0.0, 0.0, 0.0);
    tmpvar_6 = tmpvar_15;
  };
  tmpvar_5.x = tmpvar_9;
  tmpvar_5.y = tmpvar_10;
  tmpvar_1 = vec4(0.0, 0.0, 0.0, 0.0);
  highp vec2 tmpvar_24;
  tmpvar_24 = vec2(0.0, 0.0);
  highp vec4 tmpvar_25;
  if ((vTexCoord1.x == -1.0)) {
    tmpvar_25 = (tmpvar_3 * matWorldViewProj);
  } else {
    highp vec4 outPosition_26;
    highp vec4 tmpvar_27;
    tmpvar_27.zw = vec2(0.0, 1.0);
    tmpvar_27.xy = tmpvar_3.xy;
    highp vec4 tmpvar_28;
    tmpvar_28 = (tmpvar_27 * matWorldViewProj);
    outPosition_26 = (tmpvar_28 / abs(tmpvar_28.w));
    highp vec4 tmpvar_29;
    tmpvar_29.zw = vec2(0.0, 1.0);
    tmpvar_29.xy = tmpvar_3.zw;
    highp vec4 tmpvar_30;
    tmpvar_30 = (tmpvar_29 * matWorldViewProj);
    highp vec2 tmpvar_31;
    tmpvar_31 = normalize(((tmpvar_30 / 
      abs(tmpvar_30.w)
    ).xy - outPosition_26.xy));
    outPosition_26.x = (outPosition_26.x - ((tmpvar_4.x * screenSize.z) * tmpvar_31.y));
    outPosition_26.y = (outPosition_26.y + ((tmpvar_4.x * screenSize.w) * tmpvar_31.x));
    tmpvar_25 = outPosition_26;
  };
  highp float tmpvar_32;
  if ((vTexCoord1.x >= 0.0)) {
    tmpvar_32 = 0.0;
  } else {
    tmpvar_32 = 1.0;
  };
  tmpvar_24 = tmpvar_6.xy;
  highp vec4 tmpvar_33;
  tmpvar_33.xy = tmpvar_4;
  tmpvar_33.zw = tmpvar_5.xy;
  tmpvar_1.xzw = tmpvar_25.xzw;
  tmpvar_1.y = -(tmpvar_25.y);
  gl_Position = tmpvar_1;
  xlv_COLOR0 = mix (vColor1, vColor, vec4(tmpvar_32));
  xlv_TEXCOORD0 = tmpvar_24;
  xlv_TEXCOORD1 = tmpvar_33;
}

