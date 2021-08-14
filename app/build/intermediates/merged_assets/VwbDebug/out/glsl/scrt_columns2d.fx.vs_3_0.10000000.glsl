uniform highp vec4 screenSize;
uniform highp mat4 matWorldViewProj;
uniform highp mat4 matDataTransform;
uniform highp vec4 AnchorParams;
uniform highp vec4 PenThickness;
attribute highp vec2 vPosition;
attribute highp vec2 vTexCoord0;
attribute highp vec4 vColor;
attribute highp vec4 vColor1;
attribute highp vec2 vTexCoord1;
attribute highp vec2 vTexCoord2;
attribute highp vec4 vTexCoord3;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec2 tmpvar_2;
  highp vec2 tmpvar_3;
  highp vec4 tmpvar_4;
  highp vec4 tmpvar_5;
  highp vec2 tmpvar_6;
  highp vec2 tmpvar_7;
  highp vec4 tmpvar_8;
  highp float tmpvar_9;
  tmpvar_9 = vTexCoord1.x;
  highp vec2 tmpvar_10;
  highp vec2 tmpvar_11;
  tmpvar_11.x = matDataTransform[0].x;
  tmpvar_11.y = matDataTransform[1].y;
  highp vec2 tmpvar_12;
  tmpvar_12.x = matDataTransform[0].w;
  tmpvar_12.y = matDataTransform[1].w;
  tmpvar_10 = ((vPosition * tmpvar_11) + tmpvar_12);
  if ((vTexCoord1.x == -1.0)) {
    highp vec2 tmpvar_13;
    tmpvar_13.x = (tmpvar_10.x + mix ((
      -(vTexCoord0.x)
     * AnchorParams.x), (vTexCoord0.x * 
      (1.0 - AnchorParams.x)
    ), vTexCoord1.y));
    tmpvar_13.y = mix ((tmpvar_10.y + (vTexCoord0.y * AnchorParams.y)), (tmpvar_10.y + (vTexCoord0.y * 
      (1.0 - AnchorParams.y)
    )), vTexCoord2.x);
    tmpvar_2 = tmpvar_13;
    tmpvar_3 = vTexCoord0;
    tmpvar_4 = vColor;
    tmpvar_5 = vColor1;
    tmpvar_6 = vTexCoord1;
    tmpvar_7 = vTexCoord2;
    tmpvar_8 = vTexCoord3;
  } else {
    highp vec4 tmpvar_14;
    tmpvar_14.zw = vTexCoord3.zw;
    highp float tmpvar_15;
    tmpvar_15 = (-(vTexCoord0.x) * AnchorParams.x);
    highp float tmpvar_16;
    tmpvar_16 = (vTexCoord0.x * (1.0 - AnchorParams.x));
    highp float tmpvar_17;
    tmpvar_17 = (tmpvar_10.y + (vTexCoord0.y * AnchorParams.y));
    highp float tmpvar_18;
    tmpvar_18 = (tmpvar_10.y + (vTexCoord0.y * (1.0 - AnchorParams.y)));
    highp vec2 tmpvar_19;
    tmpvar_19.x = (tmpvar_10.x + mix (tmpvar_15, tmpvar_16, vTexCoord1.y));
    highp float tmpvar_20;
    tmpvar_20 = (vTexCoord3.w * PenThickness.x);
    tmpvar_19.y = (mix (tmpvar_17, tmpvar_18, vTexCoord2.x) - tmpvar_20);
    highp vec2 tmpvar_21;
    tmpvar_21.x = (tmpvar_10.x + mix (tmpvar_15, tmpvar_16, vTexCoord3.x));
    tmpvar_21.y = (mix (tmpvar_17, tmpvar_18, vTexCoord2.y) - tmpvar_20);
    highp vec2 x_22;
    x_22 = (tmpvar_19 - tmpvar_21);
    highp vec2 tmpvar_23;
    tmpvar_23.y = 0.0;
    tmpvar_23.x = ((PenThickness.x * vTexCoord3.z) * 1.01);
    highp vec2 tmpvar_24;
    tmpvar_24.x = 0.0;
    tmpvar_24.y = ((sqrt(
      dot (x_22, x_22)
    ) / PenThickness.y) * abs(vTexCoord3.y));
    tmpvar_14.xy = tmpvar_24;
    tmpvar_2 = mix (tmpvar_19, tmpvar_21, vTexCoord3.yy);
    tmpvar_3 = vTexCoord0;
    tmpvar_4 = vColor;
    tmpvar_5 = vColor1;
    tmpvar_6 = tmpvar_23;
    tmpvar_7 = mix (tmpvar_21, tmpvar_19, vTexCoord3.yy);
    tmpvar_8 = tmpvar_14;
  };
  tmpvar_3.y = tmpvar_9;
  tmpvar_1 = vec4(0.0, 0.0, 0.0, 0.0);
  highp vec4 tmpvar_25;
  highp vec2 tmpvar_26;
  highp vec4 tmpvar_27;
  tmpvar_25 = vec4(0.0, 0.0, 0.0, 0.0);
  tmpvar_26 = vec2(0.0, 0.0);
  tmpvar_27 = vec4(0.0, 0.0, 0.0, 0.0);
  highp vec4 tmpvar_28;
  if ((vTexCoord1.x == -1.0)) {
    highp vec4 tmpvar_29;
    tmpvar_29.zw = vec2(0.0, 1.0);
    tmpvar_29.xy = tmpvar_2;
    tmpvar_28 = (tmpvar_29 * matWorldViewProj);
  } else {
    highp vec4 outPosition_30;
    highp vec4 tmpvar_31;
    tmpvar_31.zw = vec2(0.0, 1.0);
    tmpvar_31.xy = tmpvar_2;
    highp vec4 tmpvar_32;
    tmpvar_32 = (tmpvar_31 * matWorldViewProj);
    outPosition_30 = (tmpvar_32 / abs(tmpvar_32.w));
    highp vec4 tmpvar_33;
    tmpvar_33.zw = vec2(0.0, 1.0);
    tmpvar_33.xy = tmpvar_7;
    highp vec4 tmpvar_34;
    tmpvar_34 = (tmpvar_33 * matWorldViewProj);
    highp vec2 tmpvar_35;
    tmpvar_35 = normalize(((tmpvar_34 / 
      abs(tmpvar_34.w)
    ).xy - outPosition_30.xy));
    outPosition_30.x = (outPosition_30.x - ((tmpvar_6.x * screenSize.z) * tmpvar_35.y));
    outPosition_30.y = (outPosition_30.y + ((tmpvar_6.x * screenSize.w) * tmpvar_35.x));
    tmpvar_28 = outPosition_30;
  };
  if ((tmpvar_6.x == -1.0)) {
    tmpvar_25 = tmpvar_4;
  } else {
    tmpvar_25 = tmpvar_5;
  };
  tmpvar_26 = tmpvar_8.xy;
  tmpvar_27.xy = tmpvar_6;
  tmpvar_27.z = tmpvar_3.y;
  tmpvar_1.xzw = tmpvar_28.xzw;
  tmpvar_1.y = -(tmpvar_28.y);
  gl_Position = tmpvar_1;
  xlv_COLOR0 = tmpvar_25;
  xlv_TEXCOORD0 = tmpvar_26;
  xlv_TEXCOORD1 = tmpvar_27;
}

