uniform highp vec4 screenSize;
uniform highp mat4 matWorldViewProj;
uniform highp vec4 PackedFloatParams;
uniform highp vec4 WaterFallSliceOffsset;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 WorldXZOffsetSize;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec4 vNormal;
attribute highp vec3 vTexCoord0;
attribute highp vec4 vTexCoord1;
attribute highp vec4 vColor;
attribute highp vec4 vColor1;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  highp vec4 tmpvar_3;
  highp vec4 tmpvar_4;
  tmpvar_4 = mix (vPosition, vNormal, vTexCoord1.yyyy);
  highp vec4 tmpvar_5;
  tmpvar_5 = mix (vNormal, vPosition, vTexCoord1.yyyy);
  highp vec3 tmpvar_6;
  tmpvar_6.x = (vTexCoord0.x * vTexCoord1.z);
  tmpvar_6.y = vTexCoord1.x;
  tmpvar_6.z = vTexCoord0.z;
  tmpvar_1.xyz = tmpvar_4.xyz;
  tmpvar_1.w = 1.0;
  tmpvar_2.xyw = tmpvar_1.xyw;
  tmpvar_3.xyw = tmpvar_5.xyw;
  tmpvar_2.z = (tmpvar_4.z + WaterFallSliceOffsset.y);
  tmpvar_3.z = (tmpvar_5.z + WaterFallSliceOffsset.y);
  highp vec2 tmpvar_7;
  tmpvar_7.x = tmpvar_2.x;
  tmpvar_7.y = WaterFallSliceOffsset.y;
  highp vec2 _texCoord_8;
  highp vec2 tmpvar_9;
  tmpvar_9 = (1.0/(WorldXZOffsetSize.zw));
  _texCoord_8 = ((tmpvar_7 - WorldXZOffsetSize.xy) * tmpvar_9);
  _texCoord_8.x = mix (TextureDimensionsInv.x, TextureDimensionsInv.z, _texCoord_8.x);
  _texCoord_8.y = mix (TextureDimensionsInv.y, TextureDimensionsInv.w, _texCoord_8.y);
  lowp vec4 tmpvar_10;
  tmpvar_10 = texture2DLod (HeightMapTexture, _texCoord_8, 0.0);
  highp vec4 rgba_11;
  rgba_11 = tmpvar_10.zyxw;
  tmpvar_2.y = clamp ((tmpvar_4.y * (
    (dot (rgba_11.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y)
   + PackedFloatParams.x)), WaterFallSliceOffsset.z, WaterFallSliceOffsset.w);
  highp vec2 tmpvar_12;
  tmpvar_12.x = tmpvar_3.x;
  tmpvar_12.y = WaterFallSliceOffsset.y;
  highp vec2 _texCoord_13;
  _texCoord_13 = ((tmpvar_12 - WorldXZOffsetSize.xy) * tmpvar_9);
  _texCoord_13.x = mix (TextureDimensionsInv.x, TextureDimensionsInv.z, _texCoord_13.x);
  _texCoord_13.y = mix (TextureDimensionsInv.y, TextureDimensionsInv.w, _texCoord_13.y);
  lowp vec4 tmpvar_14;
  tmpvar_14 = texture2DLod (HeightMapTexture, _texCoord_13, 0.0);
  highp vec4 rgba_15;
  rgba_15 = tmpvar_14.zyxw;
  tmpvar_3.y = clamp ((tmpvar_5.y * (
    (dot (rgba_15.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y)
   + PackedFloatParams.x)), WaterFallSliceOffsset.z, WaterFallSliceOffsset.w);
  highp vec4 tmpvar_16;
  highp vec2 tmpvar_17;
  highp vec4 outPosition_18;
  highp vec4 tmpvar_19;
  tmpvar_19.w = 1.0;
  tmpvar_19.xyz = tmpvar_2.xyz;
  highp vec4 tmpvar_20;
  tmpvar_20 = (tmpvar_19 * matWorldViewProj);
  outPosition_18 = (tmpvar_20 / abs(tmpvar_20.w));
  highp vec4 tmpvar_21;
  tmpvar_21.w = 1.0;
  tmpvar_21.xyz = tmpvar_3.xyz;
  highp vec4 tmpvar_22;
  tmpvar_22 = (tmpvar_21 * matWorldViewProj);
  highp vec2 tmpvar_23;
  tmpvar_23 = normalize(((tmpvar_22 / 
    abs(tmpvar_22.w)
  ).xy - outPosition_18.xy));
  outPosition_18.x = (outPosition_18.x - ((tmpvar_6.x * screenSize.z) * tmpvar_23.y));
  outPosition_18.y = (outPosition_18.y + ((tmpvar_6.x * screenSize.w) * tmpvar_23.x));
  tmpvar_16.xyw = outPosition_18.xyw;
  tmpvar_16.z = (outPosition_18.z - (outPosition_18.z * 1e-5));
  tmpvar_17.x = (0.5 * (1.0 - (
    (vTexCoord0.z - 2.0)
   / vTexCoord0.z)));
  tmpvar_17.y = tmpvar_6.y;
  gl_Position = tmpvar_16;
  xlv_COLOR0 = mix (vColor, vColor1, vTexCoord1.yyyy);
  xlv_TEXCOORD0 = tmpvar_17;
}

